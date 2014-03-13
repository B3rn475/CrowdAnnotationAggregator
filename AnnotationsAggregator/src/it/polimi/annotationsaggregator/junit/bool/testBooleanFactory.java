package it.polimi.annotationsaggregator.junit.bool;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;

import it.polimi.annotationsaggregator.Aggregator;
import it.polimi.annotationsaggregator.Annotator;
import it.polimi.annotationsaggregator.CoherenceEstimator;
import it.polimi.annotationsaggregator.CoherenceEstimator.OnEstimationCompletedListener;
import it.polimi.annotationsaggregator.Content;
import it.polimi.annotationsaggregator.Aggregator.OnAggregationCompletedListener;
import it.polimi.annotationsaggregator.Pair;
import it.polimi.annotationsaggregator.bool.BooleanAnnotation;
import it.polimi.annotationsaggregator.bool.BooleanFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testBooleanFactory implements OnAggregationCompletedListener<BooleanAnnotation>, OnEstimationCompletedListener<BooleanAnnotation>{

	private BooleanFactory factory;
	Annotator[] annotators;
	Content[] contents;
	boolean[] expectedAggregatedBooleans;
	boolean expectedFinalAnnotation;
	double expectedEstimatedWeight;
	
	@Before
	public void setUp() throws Exception {
		factory = new BooleanFactory();
		annotators = new Annotator[] { new Annotator(1), new Annotator(2), new Annotator(3) };
		expectedAggregatedBooleans = new boolean[] {false, true, true};
		contents = new Content[] {new Content(1), new Content(2), new Content(3)};
		expectedFinalAnnotation = false;
		expectedEstimatedWeight = 1.0;
	}

	@After
	public void tearDown() throws Exception {
		factory = null;
		annotators = null;
		expectedAggregatedBooleans = null;
	}

	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanFactory#buildAggregator(it.polimi.annotationsaggregator.Content)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testBooleanFactorybuildAggregatorNullListener() {
		factory.buildAggregator(null, Content.NONE);
	}
	
	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanFactory#buildAggregator(it.polimi.annotationsaggregator.Content)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testBooleanFactorybuildAggregatorNullContent() {
		factory.buildAggregator(this, null);
	}

	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanFactory#buildAggregator(it.polimi.annotationsaggregator.Content)}.
	 */
	@Test
	public void testBooleanFactorybuildAggregatorResult() {		
		Aggregator<BooleanAnnotation> aggregator = factory.buildAggregator(this, Content.NONE);
		assertNotNull("No Aggregator built", aggregator);
		
		aggregator.add(new BooleanAnnotation(Content.NONE, annotators[0], true));
		aggregator.add(new BooleanAnnotation(Content.NONE, annotators[1], false));
		aggregator.add(new BooleanAnnotation(Content.NONE, annotators[2], false));
		
		Hashtable<Annotator, Double> weights = new Hashtable<Annotator, Double>();
		weights.put(annotators[0], 1.0/3.0);
		weights.put(annotators[1], 1.0/3.0);
		weights.put(annotators[2], 1.0/3.0);
		
		aggregator.aggregate(weights);
		
		aggregator.aggregateFinal(weights);
	}

	@Override
	public void onAggregationCompleted(Aggregator<BooleanAnnotation> sender,
			Collection<Pair<BooleanAnnotation>> aggregatedAnnotations) {
		assertNotNull("Sender is null",sender);
		assertNotNull("aggregatedAnnotations is null",aggregatedAnnotations);
		assertEquals("Less annotations has been generated",aggregatedAnnotations.size(), annotators.length);
		
		HashSet<Annotator> viewedAnnotators = new HashSet<Annotator>();
		
		for (Pair<BooleanAnnotation> pair : aggregatedAnnotations){
			assertNotNull("Annotation annotator is null", pair.annotation.annotator);
			assertNotNull("Estimation annotator is null", pair.estimation.annotator);
			assertEquals("Annotation and Estimation are different", pair.annotation.annotator,pair.estimation.annotator);
			viewedAnnotators.add(pair.annotation.annotator);
			for (int i=0; i < annotators.length; i++){
				if (annotators[i].equals(pair.annotation.annotator)){
					assertEquals("Estimation is incorrect", pair.estimation.getValue(), expectedAggregatedBooleans[i]);
					break;
				}
			}
		}
		
		for (Annotator annotator : annotators){
			assertTrue("Annotator not present", viewedAnnotators.contains(annotator));
		}
	}

	@Override
	public void onFinalAggregationCompleted(
			Aggregator<BooleanAnnotation> sender,
			BooleanAnnotation aggregatedAnnotation) {
		assertNotNull("Sender is null",sender);
		assertNotNull("aggregatedAnnotation is null",aggregatedAnnotation);
		
		assertEquals("Estimation is incorrect", aggregatedAnnotation.getValue(), expectedFinalAnnotation);
	}
	
	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanFactory#buildEstimator}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testBooleanFactorybuildEstimatorNullListener() {
		factory.buildEstimator(null, Annotator.NONE);
	}
	
	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanFactory#buildEstimator}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testBooleanFactorybuildEstimatorNullContent() {
		factory.buildEstimator(this, null);
	}

	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanFactory#buildEstimator}.
	 */
	@Test
	public void testBooleanFactorybuildEstimatorResult() {
		CoherenceEstimator<BooleanAnnotation> estimator = factory.buildEstimator(this, Annotator.NONE);
		assertNotNull("No Estimator built", estimator);
		
		estimator.add(new Pair<BooleanAnnotation>(new BooleanAnnotation(contents[0], Annotator.NONE, true), new BooleanAnnotation(contents[0], Annotator.NONE, true)));
		estimator.add(new Pair<BooleanAnnotation>(new BooleanAnnotation(contents[1], Annotator.NONE, true), new BooleanAnnotation(contents[0], Annotator.NONE, true)));
		estimator.add(new Pair<BooleanAnnotation>(new BooleanAnnotation(contents[2], Annotator.NONE, false), new BooleanAnnotation(contents[0], Annotator.NONE, true)));
		
		estimator.estimate();
	}

	@Override
	public void onEstimationCompleted(
			CoherenceEstimator<BooleanAnnotation> sender, double weight) {
		assertNotNull("Sender is null",sender);
		assertEquals("Estimate weight", weight, expectedEstimatedWeight, 0.001);
	}
}
