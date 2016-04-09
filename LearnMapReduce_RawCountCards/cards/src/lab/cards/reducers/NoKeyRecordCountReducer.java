package lab.cards.reducers;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class NoKeyRecordCountReducer extends
		Reducer<Text, IntWritable, NullWritable, IntWritable> { // input key type: Text; input value type: IntWritable; output key type: NullWritable; output value type: IntWritable;

	@Override
	// reduce function: input: <"count", {1,1,1,1}>; output: 52;
	// this reduce function will be called one time, coz only one unique key: "count"
	public void reduce(Text key, Iterable<IntWritable> records, Context context)
			throws IOException, InterruptedException { // Text: reducers input key; Iterable<IntWritable>: (array) reducers input value; Context: reducers output
		int sum = 0;

		for (IntWritable record : records) {
			sum += record.get();
		}

		// return to HDFS
		context.write(NullWritable.get(), new IntWritable(sum)); // NullWritable: key type = NULL
		//output for our largedeck
		//count	54525952
		
		//output for out deckofcards
		//count	52
		
	}
}
