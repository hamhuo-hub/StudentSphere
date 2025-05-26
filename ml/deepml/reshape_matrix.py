import numpy as np

def reshape_matrix(a: list[list[int|float]], new_shape: tuple[int, int]) -> list[list[int|float]]:
	#Write your code here and return a python list after reshaping by using numpy's tolist() method
	numpy_array = np.array(a)
	try:
		reshape_matrix = numpy_array.reshape(new_shape).tolist()
	except ValueError:
		reshape_matrix = []
	return reshape_matrix


print(reshape_matrix([[1,2,3,4],[5,6,7,8]], (4, 2)))
print(reshape_matrix([[1, 2, 3, 4], [5, 6, 7, 8]], (1, 4)))
print(reshape_matrix([[1,2,3],[4,5,6]], (3, 2)))
print(reshape_matrix([[1,2,3,4],[5,6,7,8]], (2, 4)))