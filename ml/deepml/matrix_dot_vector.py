def matrix_dot_vector(a: list[list[int | float]], b: list[int | float]) -> list[int | float]:
    # Return a list where each element is the dot product of a row of 'a' with 'b'.
    # If the number of columns in 'a' does not match the length of 'b', return -1.
    # if not used to check empty
    if not a or not b:
        return -1

    num_cols = len(a[0])

    # make sure A is a validate matrix
    if any(len(row) != num_cols for row in a):
        return -1

    # make sure matrix can caculate
    if len(b) != num_cols:
        return -1

    result = [sum(x * y for x, y in zip(row, b)) for row in a]

    return result
