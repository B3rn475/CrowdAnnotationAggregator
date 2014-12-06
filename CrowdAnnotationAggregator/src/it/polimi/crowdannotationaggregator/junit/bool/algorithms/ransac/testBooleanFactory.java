package it.polimi.crowdannotationaggregator.junit.bool.algorithms.ransac;

import static org.junit.Assert.*;
import it.polimi.crowdannotationaggregator.algorithms.ransac.Aggregator;
import it.polimi.crowdannotationaggregator.algorithms.ransac.InlierEstimator;
import it.polimi.crowdannotationaggregator.examples.bool.algorithms.ransac.BooleanFactory;
import it.polimi.crowdannotationaggregator.examples.bool.models.BooleanAnnotation;
import it.polimi.crowdannotationaggregator.models.Annotator;
import it.polimi.crowdannotationaggregator.models.Content;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testBooleanFactory implements Aggregator.OnAggregationCompletedListener<BooleanAnnotation, Content>, InlierEstimator.OnEstimationCompletedListener<BooleanAnnotation, Content>{

	private BooleanFactory factory;
	private Annotator[] annotators;
	private Content[] contents;
	private BooleanAnnotation[] annotationsA;
	private BooleanAnnotation[] annotationsE;
	private Set<Annotator> choosenSet;
	private Map<Content, BooleanAnnotation> model;
	
	@Before
	public void setUp() throws Exception {
		factory = new BooleanFactory();
		annotators = new Annotator[]{ new Annotator(1), 
				new Annotator(2), 
				new Annotator(3) };
		contents = new Content[] {new Content(1), 
				new Content(2), 
				new Content(3)};
		annotationsA = new BooleanAnnotation[] { new BooleanAnnotation(contents[0], annotators[0], true),
				 new BooleanAnnotation(contents[0], annotators[1], true),
				 new BooleanAnnotation(contents[0], annotators[2], false),
		};
		annotationsE = new BooleanAnnotation[] { new BooleanAnnotation(contents[0], annotators[0], true),
				 new BooleanAnnotation(contents[1], annotators[0], true),
				 new BooleanAnnotation(contents[2], annotators[0], false),
		};
		choosenSet = new HashSet<Annotator>();
		choosenSet.add(annotators[0]);
		model = new HashMap<Content, BooleanAnnotation>();
		model.put(contents[0], annotationsE[0]);
	}

	@After
	public void tearDown() throws Exception {
		contents = null;
		factory = null;
		annotators = null;
		annotationsA = null;
		annotationsE = null;
		choosenSet = null;
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
		
		aggregator.aggregate(choosenSet);
	}

	@Override
	public void onAggregationCompleted(Aggregator<BooleanAnnotation, Content> sender,
			BooleanAnnotation aggregatedAnnotation) {
		assertNotNull("Sender is null",sender);
		assertNotNull("aggregatedAnnotations is null",aggregatedAnnotation);
		assertEquals(true, aggregatedAnnotation.getValue());
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
		InlierEstimator<BooleanAnnotation, Content> estimator = factory.buildEstimator(this, annotators[0]);
		assertNotNull("No Estimator built", estimator);
		
		estimator.add(annotationsE[0]);
		estimator.add(annotationsE[1]);
		estimator.add(annotationsE[2]);
		
		estimator.estimate(model);
	}

	@Override
	public void onEstimationCompleted(
			InlierEstimator<BooleanAnnotation, Content> sender, boolean inlier) {
		assertNotNull("Sender is null",sender);
		assertEquals(true, inlier);
	}

	@Override
	public void onAggregationCompleted(
			Aggregator<BooleanAnnotation, Content> sender) {
		assertNotNull("Sender is null",sender);
	}
}
