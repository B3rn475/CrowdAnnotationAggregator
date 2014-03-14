package it.polimi.annotationsaggregator.junit.bool;

import static org.junit.Assert.*;

import java.util.Map;

import it.polimi.annotationsaggregator.AggregationManager;
import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.BaseAggregationManager.OnBaseProcessListener;
import it.polimi.annotationsaggregator.Content;
import it.polimi.annotationsaggregator.bool.BooleanAggregationManager;
import it.polimi.annotationsaggregator.bool.BooleanAnnotation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testBooleanAggregationManager implements OnBaseProcessListener<BooleanAnnotation> {

	BooleanAggregationManager manager;
	Annotator[] annotators;
	Content[] contents;
	int annotatorsNumber = 3;
	int contentsNumber = 2;
	
	@Before
	public void setUp() throws Exception {
		annotators = new Annotator[annotatorsNumber];
		contents = new Content[contentsNumber];
		manager = new BooleanAggregationManager(this, 0.01, 100);
		
		for (int k=0; k < contents.length; k++){
			contents[k] = new Content(k+1);
		}
		
		double weight = 1.0 / annotators.length / contents.length;
		
		for (int i=0; i < annotators.length; i++){
			annotators[i] = new Annotator(i+1);
			boolean value = i < annotatorsNumber / 2 + 1;
			for (int k=0; k < contents.length; k++){
				manager.put(new BooleanAnnotation(contents[k], annotators[i], value),weight);
				value = !value;
			}
		}
	}

	@After
	public void tearDown() throws Exception {
		manager = null;
		annotators = null;
		contents = null;
	}

	@Test
	public void test() {
		manager.startProcess();
	}

	@Override
	public void onStepInitiated(AggregationManager<BooleanAnnotation, Content> sender, int step) {
		assertTrue("Step is negative", step >= 0);
	}

	@Override
	public void onAggregationEnded(AggregationManager<BooleanAnnotation, Content> sender, Map<Content, BooleanAnnotation> aggregatedAnnotations) {
		boolean value = true;
		for (int k=0; k < contents.length; k++){
			assertEquals("Estimation Error", value, aggregatedAnnotations.get(contents[k]).getValue());
			value = !value;
		}
	}

}
