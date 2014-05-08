AnnotationsAggregator
=====================
_By Carlo Bernaschina (GitHub - B3rn475) from Politecnico di Milano (Como campus)_

AnnotationsAggregator is a Java Library that allow to aggregated the annotations of crowdsourcing system regardless of the specific type of annotations.

It solves the problem relying on the implementation of two custom function that are dependent on the type of annotation and must be implemented for the specific case.

Base Classes
---------------------

__Content__  
This class rapresents the object that is going to be annotated. It has by default only one field __id__ that is used to identify it. The __id__ must be grater than __0__.  
The __id__ is the only field that is used for identification so two Content with the same __id__ will be taken in account as they are the same.  
If you need to add informations you can just implement a class that inherits from it and add the fields you need.

__Annotator__  
This class rapresents the user that annotates a content. It has an __id__ that is used to identify it. This __id__ must be grater than __0__ (0 means no Annotator and is accessible via Annotator.NONE).  
The __id__ identifies the Annotator so two Annotators with the same __id__ will be taken in account as they are the same.  
If you need to add informations you can just implement a class that inherits from it and add the fields you need.

__Annotation__ (_Abstract_)  
This class rapresents the annotation given by an Annotator to a Content.
It is templated on both Content and Annotator so you can use informations stored in custom Content and Annotator implementations. If you do not need that you can just use __BaseAnnotation__.  
This is an Abstract class so you need to implement your own Annotation. It has to contain the data needed by both __Aggregator__ and __CoherenceEstimator__.

__AggregationManager__  
This is the heart of the system, it is the class responsible of the Algorithm management.  
It is templated on both __Annotation__ and __Content__ (the __Content__ templated of the annotation must be the same of the __Annotation__).  
If you are not going to create a custom __Content__ you can use the __BaseAggreationManager__ that is templated only on the __Annotation__ that must be a class derived by __BaseAnnotation__.  
It is a Map<Annotation, Double> so it allows to insert both the __Annotation__s and a start value for the weight associated to that __Annotation__ (set the weight to 1 to use the default initialization).  
It has been thinked has an asynchronous Object so once the start() method is called it may return imediatelly. The completion is signaled through the Listener callback.  
The asynchronicity if the Object is dependent on the asynchronicity if both __Aggregator__s and __CoherenceEstimator__s, if at least one of them is Asynchronous the Object is Asynchronous.

__Aggregator__  
This is the class responsible of aggregating a group of __Annotation__s taking in account the weights given to each __Annotation__.  
It is templated on both the __Annotation__ and the __Content__. If you are not going to use a custom __Content__ you can use __BaseAggregator__ and so the __Annotation__ will be a subclass of __BaseAnnotation__.  
You must define your own __Aggregator__ that implements an algorithm valid for you particular kind of __Annotation__.  
During the process many Objects of this type will be instantiated. In particualar one for each __Content__, so you are sure that all the __Annotation__s passed are related to the same __Content__.  
You have to implement the method __aggregate__ that takes as parameter an __Annotator__ to skeep in the process.  
This method can be Asynchronous but has to call the method __postAggregate__ at the end of the process. The __Annotation__ passed to the __postAggregate__ must be related to the same __Annotator__ passed as skip parameter.  
You can access the current __Annotation__s due to the fact that the Object is a __Collection__ of __Annotation__s.  
You can access the current weight using the method __getWeights__.
If for performance reason you want to precompute some values at the beginning of the process you can do that Overriding the method __initializingAggregation__.  It can be Asynchronous, you must invoke __postInitializingAggregation__ at the end of the initialization.  
If you have Overrided the method __initializingAggregation__ and you want to clean up temporary properties at the end you can Override the method __endingAggregation__.  It can be Asynchronous, you must invoke __postEndingAggregation__ at the end of the tear down.  


__LinearAggregator__  
If your aggregation algorithm is linear (A+B)+C = A+(B+C) you can use instead of the standard __Aggregator__ the __LinearAggregator__ (or the __BaseLinearAggregator__) to obtain better performance.
You must implement the methods __sumAllAnnotations__ and __subtractAnnotation__.
__sumAllAnnotations__ has to sum all the __Annotation__s and report the total aggregated __Annotation__, it can be Asynchronous, you must invoke __postSubtractedAnnotation__ at the end of the process.  
__subtractAnnotation__ has to subtract a gived __Annotation__ from the Total one using the given weight, it can be Asynchronous, you must invoke __postSubtractAnnotation__ at the end of the process.  

__CoherenceEstimator__
This is the class responsible of computing the coherence among the aggregated __Annotation__s.  
It is templated on both the __Annotation__ and  the __Content__.  
You must define your own __CoherenceEstimator__ that implements an algorithm valid for you particular kind of __Annotation__.  
During the process many Objects of this type will be instantiated. In particualar one for each __Annotator__, so you are sure that all the __Annotation__s passed are related to the same __Annotator__.  
You have to implement the method __estimate__ that takes as parameter a __Content__ to skip in the process.  
This method can be Asynchronous but has to call the method __postEstimate__ at the end of the process. To this method you have to pass as parameters the __Content__ that you have skipped and the weight __estimate__ as a Double.  
If for performance reason you want to precompute some values at the beginning of the process you can do that Overriding the method __initializingEstimation__.  It can be Asynchronous, you must invoke __postInitializingEstimation__ at the end of the initialization.  
If you have Overrided the method __initializingEstimation__ and you want to clean up temporary properties at the end you can Override the method __endingEstimation__.  It can be Asynchronous, you must invoke __postEndingEstimation__ at the end of the tear down.  

__LinearAggregator__  
If your aggregation algorithm is linear (A+B)+C = A+(B+C) you can use instead of the standard __CoherenceEstimator__ the __LinearCoherenceEstimator__ to obtain better performance.
You must implement the method __comparePair__, it has to compair __Annotation__s in couples, it can be Asynchronous, you must invoke __postCompairPair__ at the end of the process.  

__AggregatorFactory__  
This interface must be implemented by a class and is used to allow the __AggregationManager__ to build new __Aggregator__s and new __CoherenceEstimator__s without the need to know their real class.

Example
---------------------------
For a basic example (that is not asynchronous) you can see the classes at __it.polimi.annotationsaggregator.bool__ and __it.polimi.annotationsaggregator.junit.bool__.