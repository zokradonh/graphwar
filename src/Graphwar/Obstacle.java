//  Copyright (C) 2011 Lucas Catabriga Rocha <catabriga90@gmail.com>
//    
//  This file is part of Graphwar.
//
//  Graphwar is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  Graphwar is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.

//  You should have received a copy of the GNU General Public License
//  along with Graphwar.  If not, see <http://www.gnu.org/licenses/>.

package Graphwar;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import GraphServer.Constants;

public class Obstacle
{
	private	BufferedImage terrain;
	private Graphics2D terrainGraphics;
	
	int expX;
	int expY;
	int expRadius;

	final int emptyPixel = 0;

	Obstacle(int numCircles, int circleInfo[])
	{
		terrain = new BufferedImage(Constants.PLANE_LENGTH, Constants.PLANE_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);

		terrainGraphics = terrain.createGraphics();

		Graphics2D g = (Graphics2D) terrainGraphics.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.DARK_GRAY);
		
		for(int i=0; i<numCircles; i++)
		{			
			int x = circleInfo[3*i];
			int y = circleInfo[3*i+1];
			int radius = circleInfo[3*i+2];
			
			g.fillOval(x-radius, y-radius, 2*radius, 2*radius);
		}

		g.dispose();
		
		expX = 0;
		expY = 0;
		expRadius = 0;
	}
		
	public static int getNumCircles()
	{
		Random random = new Random();
		
		int numCircles = (int)(random.nextGaussian()*Constants.NUM_CIRCLES_STANDARD_DEVIATION+Constants.NUM_CIRCLES_MEAN_VALUE);
		
		while(numCircles < 0)
		{
			numCircles = (int)(random.nextGaussian()*Constants.NUM_CIRCLES_STANDARD_DEVIATION+Constants.NUM_CIRCLES_MEAN_VALUE);
		}	
		
		return numCircles;
	}
	
	public int getExplosionX()
	{
		return expX;
	}
	
	public int getExplosionY()
	{
		return expY;
	}
	
	public int getExplosionRadius()
	{
		return expRadius;
	}
	
	public boolean collidePoint(int x, int y)
	{		
		if(x<0 || x>=Constants.PLANE_LENGTH)
			return true;
		
		if(y<0 || y>=Constants.PLANE_HEIGHT)
			return true;
		
		if(terrain.getRGB(x, y) != emptyPixel)
			return true;
		
		return false;
	}
	
	public void setExplosion(int x, int y, int radius)
	{
		expX = x;
		expY = y;
		expRadius = radius;
	}
	
	public void explodePoint()
	{
		terrainGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		terrainGraphics.setColor(new Color(0,0,0,0));
		terrainGraphics.fillOval(expX-expRadius, expY-expRadius, expRadius*2, expRadius*2);
	}
	
	public boolean soldierCollides(int x, int y, int radius)
	{		
		if(x+radius >= Constants.PLANE_LENGTH)
		{
			return true;
		}
		if(x-radius < 0)
		{
			return true;
		}		
		if(y+radius >= Constants.PLANE_HEIGHT)
		{
			return true;
		}
		if(y-radius < 0)
		{
			return true;
		}

		if (terrain.getRGB(x, y) == emptyPixel
				&& terrain.getRGB(x + radius, y) == emptyPixel
				&& terrain.getRGB(x - radius, y) == emptyPixel
				&& terrain.getRGB(x, y + radius) == emptyPixel
				&& terrain.getRGB(x, y - radius) == emptyPixel) {
			return false;
		}
		
		return true;
	}
	
	public Image getImage()
	{
		return terrain;
	}
	
}
