
import java.io.IOException;

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


public class Pairs {

	public static class Map extends	Mapper<LongWritable, Text, Text, IntWritable> {
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] words = value.toString().split("\\s+");
			for (int pos=0; pos<words.length-1; pos++) {
				String word = words[pos];
				if (word.matches("^\\w+$")) {
					for (int i = pos + 1; i < words.length; i++) {
						String term = words[i];
						if (!term.matches("^\\w+$"))
							continue;

						if (term.equals(word))
							break;

						else {
							context.write(new Text(word + "," + term), new IntWritable(1));
							context.write(new Text(word + ",*"), new IntWritable(1));
						}
					}
					
				}
			}
		}
	}
	
	public static class Reduce extends Reducer<Text, IntWritable, Text, Text> {
		Integer totalCount = 0;

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			String[] pair = key.toString().split(",");
			String neighbor = pair[1];

			if (neighbor.equals("*")) {
				totalCount = 0;
				for (IntWritable value : values) {
					totalCount += value.get();
				}
			}
			else {
				Integer count = 0;
				for (IntWritable value : values) {
					count += value.get();
				}
				String sFreq = "(" + count.toString() + "/" + totalCount.toString() + ")";
				context.write(key, new Text(sFreq));
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception{
		Configuration conf = new Configuration();
		
		Job job = new Job(conf);
		job.setJarByClass(Pairs.class);
		
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);

	}
}