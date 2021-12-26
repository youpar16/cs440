import util
from math import sqrt
import numpy as np

# # Helper method used by predict_classification that returns a list of the closest neighbors. 
# def get_neighbors(train, test_row, num_neighbors):
# 	distances = list()
# 	for train_row in train:
# 		# Calculate Euclidean distance between test data and each training data. 
# 		dist = 0.0
# 		for i in range(len(test_row)-1):
# 			dist += (test_row[i] - train_row[i])**2
# 		distances.append((train_row, sqrt(dist)))
			
# 	# Sorts the distances
# 	distances.sort(key=lambda tup: tup[1])
# 	# create a list of the closest neighbors using the sorted list 
# 	neighbors = list()
# 	for i in range(num_neighbors):
# 		neighbors.append(distances[i][0])
# 	return neighbors
def euclidean_distance(row1, row2):
	distance = 0.0
	for i in range(len(row1)-1):
		distance += (row1[i] - row2[i])**2
	return sqrt(distance)
		
# Helper method used by classify() to predict a classification given a list of neighbors
def predict_classification(train, test_row, num_neighbors):
	neighbors = get_neighbors(train, test_row, num_neighbors)
	output_values = [row[-1] for row in neighbors]
	prediction = max(set(output_values), key=output_values.count)
	return prediction

def get_neighbors(train, test_row, num_neighbors):
	distances = list()
	for train_row in train:
		#calculate distance of one test data and all training data using euclidean distance
		# dist = euclidean_distance(test_row, train_row)
		dist = 0.0
		for i in range(len(test_row)-1):
			dist += (test_row[i] - train_row[i])**2
		distances.append((train_row, sqrt(dist)))

	#sorting the distances
	distances.sort(key=lambda tup: tup[1])
	#create list of neighbors based on sorted distances
	neighbors = list()
	for i in range(num_neighbors):
		neighbors.append(distances[i][0])
	return neighbors

class KnnClassifier:
		def __init__( self, legalLabels, neighbors):
			self.legalLabels = legalLabels
			self.num_neighbors = neighbors

		def train(self, trainingData, trainingLabels, validationData, validationLabels):
			#initialize the data
			self.trainingData = trainingData
			self.trainingLabels = trainingLabels
			self.validationData = validationData
			self.validationLabels = validationLabels

			#make features of training data
			self.size = len(list(trainingData))
			features = []
			for datum in trainingData:
				feature = list(datum.values())
				features.append(feature)

			#combine features and labels of training data as train_set
			train_set = []
			for i in range(self.size):
				train_datum = list(np.append(features[i],self.trainingLabels[i]))
				train_set.append(train_datum)
			self.train_set = train_set

		def classify(self, testData):
			#make features of testing data
			self.size = len(list(testData))
			features = []
			for datum in testData:
				feature = list(datum.values())
				features.append(feature)

			#combine features and labels of testing data as test_set
			test_set = []
			for i in range(self.size):
				train_datum = list(np.append(features[i],None))
				test_set.append(train_datum)
			self.test_set = test_set

			#predict the class of all testing data
			guesses = []
			for test_datum in test_set:
				train_set = self.train_set
				num_neighbors = self.num_neighbors
				#call predict_classification function to predict the class of one test data
				guess = predict_classification(train_set, test_datum, num_neighbors)
				#save the data in guesses
				guesses.append(guess)
			return guesses