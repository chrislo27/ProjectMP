package projectmp.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A lot of this code was stolen from this article: <br>
 * http://gamedevelopment.tutsplus.com/tutorials/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space--gamedev-374
 * <br>
 * <br>
 * created by chrislo27
 *
 */
public class QuadTree {

	private int MAX_OBJECTS = 10;
	private int MAX_LEVELS = 12;

	private int level;
	private List<Sizeable> objects;
	private float posX, posY, width, height;
	private QuadTree[] nodes;

	/**
	 * ideal constructor for making a quadtree that's empty
	 * <br>
	 * simply calls the normal constructor with 
	 * <code>
	 * this(0, 0, 0, width, height)
	 * </code>
	 * @param width your game world width in units
	 * @param height your game world height in units
	 */
	public QuadTree(float width, float height) {
		this(0, 0, 0, width, height);
	}

	/**
	 * 
	 * @param pLevel start at level 0 if you're creating an empty quadtree
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public QuadTree(int pLevel, float x, float y, float width, float height) {
		level = pLevel;
		objects = new ArrayList();
		posX = x;
		posY = y;
		this.width = width;
		this.height = height;
		nodes = new QuadTree[4];
	}

	public QuadTree setMaxObjects(int o) {
		MAX_OBJECTS = o;
		return this;
	}

	public QuadTree setMaxLevels(int l) {
		MAX_LEVELS = l;
		return this;
	}

	public int getChecks() {
		int num = 0;

		num += (objects.size() * objects.size());

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				num += nodes[i].getChecks();
			}
		}

		return num;
	}

	/*
	 * Clears the quadtree
	 */
	public void clear() {
		objects.clear();

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}

	/*
	 * Splits the node into 4 subnodes
	 */
	private void split() {
		float subWidth = (width / 2);
		float subHeight = (height / 2);
		float x = posX;
		float y = posY;

		nodes[0] = new QuadTree(level + 1, x + subWidth, y, subWidth, subHeight);
		nodes[1] = new QuadTree(level + 1, x, y, subWidth, subHeight);
		nodes[2] = new QuadTree(level + 1, x, y + subHeight, subWidth, subHeight);
		nodes[3] = new QuadTree(level + 1, x + subWidth, y + subHeight, subWidth, subHeight);
	}

	/*
	 * Determine which node the object belongs to. -1 means object cannot
	 * completely fit within a child node and is part of the parent node
	 */
	private int getIndex(Sizeable pRect) {
		int index = -1;
		double verticalMidpoint = posX + (width / 2);
		double horizontalMidpoint = posY + (width / 2);

		// Object can completely fit within the top quadrants
		boolean topQuadrant = (pRect.getY() < horizontalMidpoint && pRect.getY()
				+ pRect.getHeight() < horizontalMidpoint);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);

		// Object can completely fit within the left quadrants
		if (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint) {
			if (topQuadrant) {
				index = 1;
			} else if (bottomQuadrant) {
				index = 2;
			}
		}
		// Object can completely fit within the right quadrants
		else if (pRect.getX() > verticalMidpoint) {
			if (topQuadrant) {
				index = 0;
			} else if (bottomQuadrant) {
				index = 3;
			}
		}

		return index;
	}

	/*
	 * Insert the object into the quadtree. If the node exceeds the capacity, it
	 * will split and add all objects to their corresponding nodes.
	 */
	public void insert(Sizeable pRect) {
		if (nodes[0] != null) {
			int index = getIndex(pRect);

			if (index != -1) {
				nodes[index].insert(pRect);

				return;
			}
		}

		objects.add(pRect);

		if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) {
				split();
			}

			int i = 0;
			while (i < objects.size()) {
				int index = getIndex(objects.get(i));
				if (index != -1) {
					nodes[index].insert(objects.remove(i));
				} else {
					i++;
				}
			}
		}
	}

	/*
	 * Return all objects that could collide with the given object
	 */
	public List retrieve(List returnObjects, Sizeable pRect) {
		int index = getIndex(pRect);
		if (index != -1 && nodes[0] != null) {
			nodes[index].retrieve(returnObjects, pRect);
		}

		returnObjects.addAll(objects);

		return returnObjects;
	}
}