import util

PRINT = True


class PerceptronClassifier:
    """
    Perceptron classifier.

    Note that the variable 'datum' in this code refers to a counter of features
    (not to a raw samples.Datum).
    """

    def __init__(self, legalLabels, max_iterations):
        self.legalLabels = legalLabels
        self.type = "perceptron"
        self.max_iterations = max_iterations
        self.weights = {}
        for label in legalLabels:
            self.weights[label] = util.Counter()  # this is the data-structure you should use

    def train(self, trainingData, trainingLabels, validationData, validationLabels):
        """
        The training loop for the perceptron passes through the training data several
        times and updates the weight vector for each label based on classification errors.
        See the project description for details.

        Use the provided self.weights[label] data structure so that
        the classify method works correctly. Also, recall that a
        datum is a counter from features to values for those features
        (and thus represents a vector a values).
        """
        
        for iteration in range(self.max_iterations):
            print ("Iteration: ", iteration)
            for i in range(len(trainingData)):
                max_score = -1
                max_score_label = None
                datum = trainingData[i]  

                for label in self.legalLabels:
                    # Calculate the prediction on the current image for every labels weights (0-9)
                    score = datum * self.weights[label]
                    if score > max_score:
                        # The score with the larges value is saved in max_score
                        max_score = score
                        max_score_label = label

                actual_label = trainingLabels[i]
                # Check if the prediction is incorrect and update the weights 
                if max_score_label != actual_label:
                    # Under
                    self.weights[actual_label] = self.weights[actual_label] + datum  
                    # Over
                    self.weights[max_score_label] = self.weights[max_score_label] - datum 

    def classify(self, data):
        """
        Classifies each datum as the label that most closely matches the prototype vector
        for that label.  See the project description for details.

        Recall that a datum is a util.counter...
        """
        predictions = []
        for datum in data:
            vectors = util.Counter()
            for l in self.legalLabels:
                vectors[l] = self.weights[l] * datum
            predictions.append(vectors.argMax())
        return predictions
#final