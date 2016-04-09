package lab.cards.drivers;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.IntWritable; // set input & output format
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import lab.cards.mappers.RecordMapper;
import lab.cards.reducers.NoKeyRecordCountReducer;

/* This program can be used for 
 * select count(1) from <table_name>;
 * Steps
 * 1. Develop mapper function
 * 2. Develop reducer function
 * 3. Job configuration
 */

public class RowCount extends Configured implements Tool { // reuse by changing class name: RowCount

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new RowCount(), args); // reuse by changing class name: RowCount
		System.exit(exitCode);

	}

	public int run(String[] args) throws Exception {
		Job job = Job.getInstance(getConf(),
				"Row Count using built in mappers and reducers"); // description of the job

		job.setJarByClass(getClass()); // here getClass() = RowCount

		FileInputFormat.setInputPaths(job, new Path(args[0])); // set input file path
		// We are not setting input format class and hence uses
		// (TextInputFormat)
		// job.setInputFormatClass(TextInputFormat.class);

		// Custom mapper (RecordMapper) to assign 1 for each record
		// Input to mapper <Lineoffset as key, entire line as value>
		// Default behavior of default input format TextInputFormat
		job.setMapperClass(RecordMapper.class); // set which mapper class need to execute: RecordMapper
		// Output from mapper
		// <count, 1>
		// <count, 1> so on

		job.setMapOutputKeyClass(Text.class); // set mapper's output key type: String 
		job.setMapOutputValueClass(IntWritable.class); // set mapper's output value type: int

		// Built-in reducer
		// Input to reducer <count, {1, 1, 1, ...}>
		// job.setReducerClass(IntSumReducer.class);
		// Output from reducer <count, Number of records>

		// Custom reducer
		// If you do not want to see "count" as part of output
		// and just see the record count as in select count query
		job.setReducerClass(NoKeyRecordCountReducer.class); // set which reducer class need to execute: NoKeyRecordCountReducer
		// Output from reducer <Number of records>

		// write the output to HDFS
		job.setOutputKeyClass(NullWritable.class); // set reducer's output key type: /
		job.setOutputValueClass(IntWritable.class); // set reducer's output value type: int

		// We are not setting output format class and hence uses default
		// (TextOutputFormat)

		// job.setOutputFormatClass(TextOutputFormat.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1])); // set output file path

		return job.waitForCompletion(true) ? 0 : 1;
	}

}
