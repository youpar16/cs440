import util
import classificationMethod
import math
import collections


class NaiveBayesClassifier(classificationMethod.ClassificationMethod):
    """
    See the project description for the specifications of the Naive Bayes classifier.

    Note that the variable 'datum' in this code refers to a counter of features
    (not to a raw samples.Datum).
    """

    def __init__(self, legalLabels):
        self.legalLabels = legalLabels
        self.type = "naivebayes"
        self.k = 1  # this is the smoothing parameter, ** use it in your train method **
        self.automaticTuning = False  # Look at this flag to decide whether to choose k automatically ** use this in your train method **

    def setSmoothing(self, k):
        """
        This is used by the main method to change the smoothing parameter before training.
        Do not modify this method.
        """
        self.k = k

    def train(self, trainingData, trainingLabels, validationData, validationLabels):
        """
        Outside shell to call your method. Do not modify this method.
        """
        # this is a list of all features in the training set.
        self.features = list(set([f for datum in trainingData for f in datum.keys()]))

        if (self.automaticTuning):
            kgrid = [0.001, 0.01, 0.05, 0.1, 0.5, 1, 5, 10, 20, 50]
        else:
            kgrid = [self.k]

        self.trainAndTune(trainingData, trainingLabels, validationData, validationLabels, kgrid)

    def trainAndTune(self, trainingData, trainingLabels, validationData, validationLabels, kgrid):
        """
        Trains the classifier by collecting counts over the training data, and
        stores the Laplace smoothed estimates so that they can be used to classify.
        Evaluate each value of k in kgrid to choose the smoothing parameter
        that gives the best accuracy on the held-out validationData.

        trainingData and validationData are lists of feature Counters.  The corresponding
        label lists contain the correct label for each datum.

        To get the list of all possible features or labels, use self.features and
        self.legalLabels.
        """
        # First create dictionary for given training labels and dictionary for final training labels and data
        labelDict = dict(collections.Counter(trainingLabels))
        finalData = dict()

        # Calculate the probabilities for each training label
        for x in labelDict.keys():
            labelDict[x] /= float(len(trainingLabels))
        
        # For each key in finalData, create a new default dictionary, to avoid KeyErrors in the future and instead create a new list()
        # when trying to access a key that is not yet in the dictionary
        for x, y in labelDict.items():
            finalData[x] = collections.defaultdict(list)

        # Now we create the list of final corresponding training labels and data
        for x, y in labelDict.items():
            # Create temp lists to hold wanted training label indexes and corresponding training data
            temp = list()
            temp2 = list()

            # Add training label indexes that match those in labelDict
            for a, b in enumerate(trainingLabels):
                if x == b:
                    temp.append(a)

            # Then add the corresponding training data to temp2
            for i in temp:
                temp2.append(trainingData[i])

            # Now that we have a list of the wanted training labels and data, we add it to our final training data dictionary
            for j in range(len(temp2)):
                for c, d in temp2[j].items():
                    finalData[x][c].append(d)

        # Calculates the count and copies labelDict keys to count
        count = [a for a in labelDict]
        # count = []
        # for x, y in labelDict:
        #     count[x] = labelDict[x]

        # Now calculate the Naive Bayes probabilities for each training data and label 
        for x in count:
            for y, z in temp2[x].items():
                d = finalData[x][y]
                probability = dict(collections.Counter(d))
                for i in probability.keys():
                    probability[i] /= float(len(d))
                finalData[x][y] = probability

        # Update parameters for later use 
        self.labelDict = labelDict
        self.count = count
        self.finalData = finalData

    def classify(self, testData):
        """
        Classify the data based on the posterior distribution over labels.

        You shouldn't modify this method.
        """
        guesses = []
        self.posteriors = []  # Log posteriors are stored for later data analysis (autograder).
        for datum in testData:
            posterior = self.calculateLogJointProbabilities(datum)
            guesses.append(posterior.argMax())
            self.posteriors.append(posterior)
        return guesses

    def calculateLogJointProbabilities(self, image):
        """
        Returns the log-joint distribution over legal labels and the datum.
        Each log-probability should be stored in the log-joint counter, e.g.
        logJoint[3] = <Estimate of log( P(Label = 3, datum) )>

        To get the list of all possible features or labels, use self.features and
        self.legalLabels.
        """

        """ In small training set the conditional probability for some locations can
            be 1 which will result in trying to calculate log(0) which is undefined.
            In this case I will just add 0 to the joint probability. """

        posteriors = util.Counter()

        # find the prediction with the highest percentage

        for label in self.count:
            probability = self.labelDict[label]
            for y, z in image.items():
                data = self.finalData[label][y]
                probability += math.log(data.get(image[y], 0.01))

            posteriors[label] = probability

        return posteriors
