def transpose_matrix(a: list[list[int|float]]) -> list[list[int|float]]:
    # zip() search and find column element and pack them
    b = [list(row) for row in zip(*a)]
    return b

print(transpose_matrix([[1,2],[3,4],[5,6]]))