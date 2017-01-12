import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
         
public class Hybrid {

	public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] words = value.toString().split("\\s+");
			for (int pos=0; pos < words.length-1; pos++) {
				String word = words[pos];
				if (word.matches("^\\w+$")) {
					for (int i = pos + 1; i < words.length; i++) {
						String term = words[i];
						if (!term.matches("^\\w+$"))
							continue;
						
						if (term.equals(word))
							break;
						else
							context.write(new Text(word + "," + term), new IntWritable(1));
					}
				}
			}
		}
	}

	public static class MyReducer extends Reducer<Text, IntWritable, Text, Text> {
		String tprev = null;
		Integer total = 0;
		Map<String, Integer> Hf = new HashMap<String, Integer>();

		@Override
		public void reduce(Text pair, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			String[] pairKey = pair.toString().split(",");
			String term = pairKey[0];
			
			if (tprev != null && !term.equals(tprev)) {
				// Hf/total to String
				String sHf = "";
				for (String key : Hf.keySet()) {
						sHf += "(" + key + "," + Hf.get(key).toString() + "/" + total.toString() + "),";
				}
				context.write(new Text(tprev), new Text(sHf));
				//reset
				total = 0;
				Hf = new HashMap<String, Integer>();
			}

			Integer sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			Hf.put(pairKey[1], sum);
			total += sum;
			tprev = term;

		}

		protected void cleanup(Context context) throws IOException,	InterruptedException {
			String sHf = "";
			for (String key : Hf.keySet()) {
				sHf += "(" + key + "," + Hf.get(key).toString() + "/" + total.toString() + "),";
			}
			context.write(new Text(tprev), new Text(sHf));
		}
	}


	public static void main(String[] args) throws Exception {
		 Configuration conf = new Configuration();
		     
		 Job job = new Job(conf, "hydrid");
		 job.setJarByClass(Hybrid.class);
		 job.setOutputKeyClass(Text.class);
		 job.setOutputValueClass(IntWritable.class);
		     
		 job.setMapperClass(MyMapper.class);
		 job.setReducerClass(MyReducer.class);
		     
		 job.setInputFormatClass(TextInputFormat.class);
		 job.setOutputFormatClass(TextOutputFormat.class);
		     
		 FileInputFormat.addInputPath(job, new Path(args[0]));
		 FileOutputFormat.setOutputPath(job, new Path(args[1]));
		     
		 job.waitForCompletion(true);
	}
}