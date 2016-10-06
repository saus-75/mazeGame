public class NodeRep {
	private int i;
	private int j;

	/**
	 * Initialises the {@code NodeRep} with the given indexes for a 2d array.
	 * @param i The row (i) index of the node.
	 * @param j The col (j) index of the node.
     */
	public NodeRep (int i, int j) {
		this.i = i;
		this.j = j;
	}

	/**
	 * Gets the row (i) index of the node.
	 * @return The row (i) index of the node.
     */
	public int geti () {
		return i;
	}

	/**
	 * Gets the column (j) index of the node.
	 * @return The column (j) index of the node.
	 */
	public int getj () {
		return j;
	}

	/**
	 * Tests whether this node rep is equal to the given {@code Object}. Equality is defined as their i and j coordinates are equal.
	 * @param other The object for which equality will be tested.
	 * @return The result of the equality test.
     */
	public boolean equals(Object other) {
		if (other instanceof NodeRep) {
			NodeRep that = (NodeRep) other;
			return (this.i == that.geti() &&
					this.j == that.getj());
		}
		return false;
	}

	/**
	 * Returns a hash code which uses prime numbers to produce different codes for
	 * {@code NodeRep}'s with differetn coordinates.
	 * @return The generated hashcode.
     */
	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash*17 + i;
		hash = hash*31 + j;
		return hash;
	}
}