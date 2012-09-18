package com.gistlabs.bigdata.littletests.hadoop.wordcount.unit;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.types.Pair;
import org.approvaltests.Approvals;
import org.approvaltests.reporters.ClipboardReporter;
import org.approvaltests.reporters.DiffReporter;
import org.approvaltests.reporters.UseReporter;
import org.approvaltests.reporters.macosx.TkDiffReporter;
import org.junit.Test;
import org.lambda.functions.Function1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * See the <a
 * href="https://cwiki.apache.org/confluence/display/MRUNIT/Index">MRUnit
 * Wiki</a> for more information.
 */
@UseReporter({ TkDiffReporter.class, ClipboardReporter.class })
public class WordCountTest extends AbstractWordCount {
	@Test
	public void testMapper() throws Exception {
		verifyMapping(new LongWritable(0), "cat cat dogg");
	}

	private void verifyMapping(LongWritable key, String input)
			throws IOException {
		mapDriver.withInput(key, new Text(input));
		List<Pair<Text, LongWritable>> run = mapDriver.run();
		Collections.sort(run, new Comparator<Pair<Text, LongWritable>>() {

			public int compare(Pair<Text, LongWritable> o1,
					Pair<Text, LongWritable> o2) {
				return o1.getFirst().compareTo(o2.getFirst());
			}

		});

		String header = String.format("[%s]\r\n\r\n -> maps to -> \r\n", input);
		Pair[] array = run.toArray(new Pair[0]);
		Approvals.verifyAll(header, array,
				new Function1<Pair<Text, LongWritable>, String>() {

					public String call(Pair<Text, LongWritable> i) {
						return "" + i;
					}
				});
	}

	@Test
	public void testReducer() {
		List<LongWritable> values = new ArrayList<LongWritable>();
		values.add(new LongWritable(1));
		values.add(new LongWritable(1));
		reduceDriver.withInput(new Text("cat"), values);
		reduceDriver.withOutput(new Text("cat"), new LongWritable(2));
		reduceDriver.runTest();
	}

	@Test
	public void testMapReduce() {
		mapReduceDriver.withInput(new LongWritable(1), new Text("cat cat dog"));
		mapReduceDriver.addOutput(new Text("cat"), new LongWritable(2));
		mapReduceDriver.addOutput(new Text("dog"), new LongWritable(1));
		mapReduceDriver.runTest();
	}
}
