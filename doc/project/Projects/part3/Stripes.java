import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
         
public class Stripes {

	public static class MyMapper extends Mapper<LongWritable, Text, Text, MapWritable> {
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String doc = value.toString();
			String[] words = doc.split("\\s+");

			// for all word in doc
			for (int wordIndex=0; wordIndex<words.length-1; wordIndex++) {
				if (!words[wordIndex].matches("^\\w+$")) // check valid word
					continue;

				Map<String, Integer> stripe = new HashMap<String, Integer>();

				// find neighbors
				for (int neighbor=wordIndex+1; neighbor<words.length; neighbor++) {
					if (!words[neighbor].matches("^\\w+$")) // check valid word
						continue;

					if (words[neighbor].equals(words[wordIndex])) 
						break;
					else 
						stripe.put(words[neighbor], stripe.containsKey(words[neighbor]) ? stripe.get(words[neighbor])+1 : 1);
				}
					

				if (!stripe.isEmpty()) {
					// convert HashMap -> MapWritable
					MapWritable mapWritable = new MapWritable();
					for (Entry<String,Integer> entry : stripe.entrySet()) {
						if(entry.getKey() != null && entry.getValue() != null)
							mapWritable.put(new Text(entry.getKey()),new IntWritable(entry.getValue()));
					}

					context.write(new Text(words[wordIndex]), mapWritable);
				}
			}
		} 
	}

	public static class MyReducer extends Reducer<Text, MapWritable, Text, Text> {
		public void reduce(Text word, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {

			MapWritable mwNeighborGather = new MapWritable();
			Integer count = 0;

			// for all H in stripes[H1,H2...]
			for (MapWritable mwNeighbor : values) {
				for (Writable neighbor : mwNeighbor.keySet()) {
					IntWritable occurences = (IntWritable) mwNeighbor.get(neighbor);
					count += occurences.get();

					if (mwNeighborGather.containsKey(neighbor)) {
						IntWritable tmp = (IntWritable) mwNeighborGather.get(neighbor);
						occurences.set(occurences.get() + tmp.get());
					}

					// add to hash result
					mwNeighborGather.put(neighbor, occurences);
				}
			}
			
			// calculate relative frequency Hf = mwNeighborGather / count
			MapWritable Hf = new MapWritable();
			String sHf = "";
			for (Writable neighbor : mwNeighborGather.keySet()) {
				IntWritable occurrences = (IntWritable) mwNeighborGather.get(neighbor);
				DoubleWritable freq = new DoubleWritable(occurrences.get() / count);
				Hf.put(neighbor, freq);

				// convert Hf to text for ouput file
				sHf += "(" + neighbor.toString() + "," + String.valueOf(occurrences.get()) + "/" + String.valueOf(count) + "),";
			}

			//context.write(word, Hf);
			context.write(word, new Text(sHf));
		}
	}


	public static void main(String[] args) throws Exception {
		 Configuration conf = new Configuration();
		     
		 Job job = new Job(conf, "stripes");
		 job.setJarByClass(Stripes.class);
		 job.setOutputKeyClass(Text.class);
		 job.setOutputValueClass(MapWritable.class);
		     
		 job.setMapperClass(MyMapper.class);
		 job.setReducerClass(MyReducer.class);
		     
		 job.setInputFormatClass(TextInputFormat.class);
		 job.setOutputFormatClass(TextOutputFormat.class);
		     
		 FileInputFormat.addInputPath(job, new Path(args[0]));
		 FileOutputFormat.setOutputPath(job, new Path(args[1]));
		     
		 job.waitForCompletion(true);
	}
}