import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder {
	private static final int maxColorDiff = 100;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points

	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;		
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {
		// TODO: YOUR CODE HERE
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				// Nested loop over every Point
				Color initialColor = new Color(image.getRGB(x,y));
				if (visited.getRGB(x, y) == 0 && colorMatch(initialColor, targetColor)) {
					// If a Point has not been visited yet and is of the correct color, continue
					Point newPoint = new Point(x, y);
					ArrayList<Point> region = new ArrayList<>();
					// Q: Are the regions the same as "toVisit"?
					region.add(newPoint);
					visited.setRGB(x, y, 1);
					// Create a Point and add it to the region
					while (region.size()>0){ // While there are points unvisited
						for (int y1 = Math.max(0, y-1); y < Math.min(image.getHeight(), y+1); y++) {
							for (int x1 = Math.max(0, x-1); x < Math.min(image.getWidth(), x+1); x++) {
								// Loop through neighboring Points, removing those that are visited
								region.remove(0); // Remove the initial Point, it is already visited
								// Q: Probably something wrong here - am I supposed to be removing this?
								// Won't it mess up the region size later on?
								Color currentColor = new Color(image.getRGB(x1, y1));
								if (colorMatch(currentColor, targetColor)) {
									// Check if the color matches with the target
									Point neighborPoint = new Point(x1, y1);
									region.add(neighborPoint);
									visited.setRGB(x1, y1, 1);
									// Add points to the region and mark them as visited


								}
							}
						}

					}
					if (region.size()>=minRegion){ // If the region is big enough to keep, add it to the list of regions
						regions.add(region);
					}


					}

				}


			}

		}




	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		// Euclidian distance squared between colors to find similarity
		int d = (c1.getRed() - c2.getRed()) * (c1.getRed() - c2.getRed())
				+ (c1.getGreen() - c2.getGreen()) * (c1.getGreen() - c2.getGreen())
				+ (c1.getBlue() - c2.getBlue()) * (c1.getBlue() - c2.getBlue());
		// If the Euclidian distance is less than our equal to our threshold, then there is a match
		if (d<=maxColorDiff){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		// TODO: YOUR CODE HERE
		ArrayList<Point> largest = new ArrayList<>(); // Create a placeholder region

		for (int counter = 0; counter < regions.size(); counter++ ) {
			// Iterate through the list of regions
			if (regions.get(counter).size() > largest.size()) { // If a region is larger than the placeholder,
				largest = regions.get(counter);                 // the region takes the place of the placeholder
			}
		}

		return largest; // Return the largest region

	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage(){
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		// TODO: YOUR CODE HERE
	}
}
