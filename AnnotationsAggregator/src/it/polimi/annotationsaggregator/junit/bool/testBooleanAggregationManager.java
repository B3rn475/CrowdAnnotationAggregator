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
	int annotatorsNumber = 5;
	int contentsNumber = 5;
	
	@Before
	public void setUp() throws Exception {
		annotators = new Annotator[annotatorsNumber + 1];
		contents = new Content[contentsNumber + 1];
		manager = new BooleanAggregationManager(this, 0.01, 100);
		
		for (int k=0; k < contentsNumber; k++){
			contents[k] = new Content(k+1);
		}
		contents[contentsNumber] = new Content(contentsNumber + 1);
			
		for (int i=0; i < annotatorsNumber; i++){
			annotators[i] = new Annotator(i+1);
			boolean value = i < annotatorsNumber / 2 + 1;
			for (int k=0; k < contentsNumber; k++){
				manager.put(new BooleanAnnotation(contents[k], annotators[i], value), 1.0);
				value = !value;
			}
		}
		annotators[annotatorsNumber] = new Annotator(annotatorsNumber + 1);
		manager.put(new BooleanAnnotation(contents[contentsNumber], annotators[annotatorsNumber]), 1.0);
	}

	@After
	public void tearDown() throws Exception {
		manager = null;
		annotators = null;
		contents = null;
	}

	@Test
	public void test() {
		assertEquals(annotatorsNumber * contentsNumber + 1, manager.size());
		manager.startProcess();
	}

	@Override
	public void onStepInitiated(AggregationManager<BooleanAnnotation, Content> sender, int step) {
		assertNotNull("Sender is null", sender);
		assertTrue("Step is negative", step >= 0);
		assertEquals(annotatorsNumber * contentsNumber, manager.size());
	}

	@Override
	public void onAggregationEnded(AggregationManager<BooleanAnnotation, Content> sender, Map<Content, BooleanAnnotation> aggregatedAnnotations) {
		assertNotNull("Sender is null", sender);
		boolean value = true;
		for (int k=0; k < contentsNumber; k++){
			assertEquals("Estimation Error", value, aggregatedAnnotations.get(contents[k]).getValue());
			value = !value;
		}
	}

	@Override
	public void onStepCompleted(
			AggregationManager<BooleanAnnotation, Content> sender, int step,
			double delta) {
		assertNotNull("Sender is null", sender);
		assertTrue("Step is negative", step >= 0);
		assertTrue("Delta is negative", delta >= 0.0);
	}

}
