package it.polimi.crowdannotationaggregator.junit.bool;

import static org.junit.Assert.*;
import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.Aggregator;
import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.BaseLinearAggregator;
import it.polimi.crowdannotationaggregator.algorithms.weightedmajorityvoting.CoherenceEstimator;
import it.polimi.crowdannotationaggregator.examples.bool.algorithms.weightedmajorityvoting.BooleanFactory;
import it.polimi.crowdannotationaggregator.examples.bool.models.BooleanAnnotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testBooleanFactory implements BaseLinearAggregator.OnBaseLinearAggregationCompletedListener<BooleanAnnotation>, CoherenceEstimator.OnEstimationCompletedListener<BooleanAnnotation, Content>{

	private BooleanFactory factory;
	Annotator[] annotators;
	Content[] contents;
	BooleanAnnotation[] annotationsA;
	BooleanAnnotation[] annotationsE;
	boolean[] expectedAggregatedBooleans;
	boolean expectedFinalAnnotation;
	double[] expectedEstimatedWeights;
	Hashtable<BooleanAnnotation, Double> weights;
	
	@Before
	public void setUp() throws Exception {
		factory = new BooleanFactory();
		annotators = new Annotator[] { new Annotator(1), 
				new Annotator(2), 
				new Annotator(3) };
		expectedAggregatedBooleans = new boolean[] {false, 
				true, 
				true};
		contents = new Content[] {new Content(1), 
				new Content(2), 
				new Content(3)};
		annotationsA = new BooleanAnnotation[] {new BooleanAnnotation(contents[0], annotators[0], true), 
				new BooleanAnnotation(contents[0], annotators[1], false),
				new BooleanAnnotation(contents[0], annotators[2], false)};
		annotationsE = new BooleanAnnotation[] {new BooleanAnnotation(contents[0], annotators[0], true), 
				new BooleanAnnotation(contents[1], annotators[0], true),
				new BooleanAnnotation(contents[2], annotators[0], false)};
		expectedFinalAnnotation = false;
		weights = new Hashtable<BooleanAnnotation, Double>();
		weights.put(annotationsA[0], 1.0);
		weights.put(annotationsA[1], 1.0);
		weights.put(annotationsA[2], 1.0);
		expectedEstimatedWeights = new double[]{0, 0, 2};
	}

	@After
	public void tearDown() throws Exception {
		contents = null;
		factory = null;
		annotators = null;
		annotationsA = null;
		annotationsE = null;
		expectedAggregatedBooleans = null;
		expectedEstimatedWeights = null;
		weights = null;
	}

	/**
	 * Test method for {@link it.polimi.annotationsaggregator.bool.BooleanFactory#buildAggregator(it.polimi.annotationsaggregator.Content)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testBooleanFactorybuildAggregatorNullListener() {
		factory.buildAggregator(null, contents[0]);
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
		Aggregator<BooleanAnnotation, Content> aggregator = factory.buildAggregator(this, contents[0]);
		assertNotNull("No Aggregator built", aggregator);
		
		aggregator.add(annotationsA[0]);
		aggregator.add(annotationsA[1]);
		aggregator.add(annotationsA[2]);
		
		aggregator.aggregate(weights);
		
		aggregator.aggregateFinal(weights);
	}

	@Override
	public void onAggregationCompleted(Aggregator<BooleanAnnotation, Content> sender,
			Map<BooleanAnnotation, BooleanAnnotation> aggregatedAnnotations) {
		assertNotNull("Sender is null",sender);
		assertNotNull("aggregatedAnnotations is null",aggregatedAnnotations);
		assertEquals("Less annotations has been generated",aggregatedAnnotations.size(), annotators.length);
		
		HashSet<Annotator> viewedAnnotators = new HashSet<Annotator>();
		
		for (Entry<BooleanAnnotation, BooleanAnnotation> entity : aggregatedAnnotations.entrySet()){
			final BooleanAnnotation annotation = entity.getKey();
			final BooleanAnnotation estimation = entity.getValue();
			assertEquals("Annotation and Estimation are different", annotation.getAnnotator(), estimation.getAnnotator());
			viewedAnnotators.add(annotation.getAnnotator());
			for (int i=0; i < annotators.length; i++){
				if (annotators[i].equals(annotation.getAnnotator())){
					assertEquals("Estimation is incorrect", estimation.getValue(), expectedAggregatedBooleans[i]);
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
			Aggregator<BooleanAnnotation,Content> sender,
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
		CoherenceEstimator<BooleanAnnotation, Content> estimator = factory.buildEstimator(this, Annotator.NONE);
		assertNotNull("No Estimator built", estimator);
		
		estimator.put(annotationsE[0], new BooleanAnnotation(contents[0], annotationsE[0].getAnnotator(), true));
		estimator.put(annotationsE[1], new BooleanAnnotation(contents[1], annotationsE[1].getAnnotator(), true));
		estimator.put(annotationsE[2], new BooleanAnnotation(contents[2], annotationsE[2].getAnnotator(), true));
		
		estimator.estimate();
	}

	@Override
	public void onEstimationCompleted(
			CoherenceEstimator<BooleanAnnotation, Content> sender, Map<BooleanAnnotation, Double> estimatedWeights) {
		assertNotNull("Sender is null",sender);
		for (int i=0; i<annotationsE.length; i++){
			System.out.println(Double.toString(estimatedWeights.get(annotationsE[i])));
			assertEquals("Estimate weight", expectedEstimatedWeights[i], estimatedWeights.get(annotationsE[i]), 0.001);
		}
	}
}
