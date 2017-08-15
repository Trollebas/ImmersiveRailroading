package cam72cam.immersiverailroading.util;

import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

/*
 * For now this just wraps the AABB constructor
 * 
 *  In the future we can override the intersects functions for better bounding boxes
 */
public class RealBB extends AxisAlignedBB {
	private double front;
	private double rear;
	private double width;
	private double height;
	private float yaw;
	private double centerX;
	private double centerY;
	private double centerZ;
	
	public RealBB(double front, double rear, double width, double height, float yaw) {
		// I hate java sometimes
		// super constructors must be the first call in a constructor, what sort
		// of shit is that
		super(c(front, rear, width, height, yaw)[0], c(front, rear, width, height, yaw)[1], c(front, rear, width, height, yaw)[2],
				c(front, rear, width, height, yaw)[3], c(front, rear, width, height, yaw)[4], c(front, rear, width, height, yaw)[5]);
		this.front = front;
		this.rear = rear;
		this.width = width;
		this.height = height;
		this.yaw = yaw;
	}
	
	private static AxisAlignedBB newBB(Vec3d min, Vec3d max) {
		//Why the fuck is this ClientOnly?
		return new AxisAlignedBB(min.x, min.y, min.z, max.x, max.y, max.z);
	}
	
	public RealBB clone() {
		RealBB clone = new RealBB(front, rear, width, height, yaw);
		clone.centerX = centerX;
		clone.centerY = centerY;
		clone.centerZ = centerZ;
		return clone;
	}

	private static double[] c(double front, double rear, double width, double height, float yaw) {
		Vec3d frontPos = VecUtil.fromYaw(front, yaw);
		Vec3d rearPos = VecUtil.fromYaw(rear, yaw);

		// width
		Vec3d offsetRight = VecUtil.fromYaw(width / 2, yaw + 90);
		Vec3d offsetLeft = VecUtil.fromYaw(width / 2, yaw - 90);
		AxisAlignedBB rightBox = newBB(frontPos.add(offsetRight), rearPos.add(offsetRight));
		AxisAlignedBB leftBox = newBB(frontPos.add(offsetLeft), rearPos.add(offsetLeft));

		AxisAlignedBB newthis = rightBox.union(leftBox);
		return new double[] { newthis.maxX, height, newthis.maxZ, newthis.minX, newthis.minY, newthis.minZ };
	}
	public AxisAlignedBB setMaxY(double y2) {
		return this.clone();
	}
	public AxisAlignedBB contract(double x, double y, double z) {
		return this.clone();
	}
	public AxisAlignedBB expand(double x, double y, double z) {
		return this.clone();
	}
	public AxisAlignedBB grow(double x, double y, double z) {
		return this.clone();
	}
	public AxisAlignedBB intersect(AxisAlignedBB p_191500_1_) {
		return this.clone();
	}
	public AxisAlignedBB union(AxisAlignedBB other) {
		return this.clone();
	}
	public AxisAlignedBB offset(double x, double y, double z) {
		RealBB offsetted = this.clone();
		offsetted.centerX += x;
		offsetted.centerY += y;
		offsetted.centerZ += z;
		return offsetted;
	}
	public AxisAlignedBB offset(BlockPos pos) {
		return this.offset(pos.getX(), pos.getY(), pos.getZ());
	}
	public double calculateXOffset(AxisAlignedBB other, double offsetX) {
		return super.calculateXOffset(other, offsetX);
	}
	public double calculateYOffset(AxisAlignedBB other, double offsetY) {
		return super.calculateYOffset(other, offsetY);
	}
	public double calculateZOffset(AxisAlignedBB other, double offsetZ) {
		return super.calculateZOffset(other, offsetZ);
	}
	public boolean intersects(double x1, double y1, double z1, double x2, double y2, double z2) {
		boolean doesIntersect = true;
		
		double actualYMin = this.centerY;
		double actualYMax = this.centerY + this.height;
		doesIntersect = doesIntersect && actualYMin < y2 && actualYMax > y1;
		
		Vec3d origin = VecUtil.rotateYaw(new Vec3d(this.rear, 0, -this.width/2), yaw).addVector(this.centerX, 0, this.centerZ);
		Vec3d point1 = VecUtil.fromYaw(front - rear, yaw).add(origin);
		Vec3d point2 = VecUtil.fromYaw(width, yaw + 90).add(origin); // might be +90
		Vec3d opposite = VecUtil.rotateYaw(new Vec3d(this.front, 0, this.width/2), yaw).addVector(this.centerX, 0, this.centerZ);
		
		
		// Scale by 100 
		int[] xp = new int[] { (int) (100 * origin.x), (int) (100 * point1.x), (int) (100 * opposite.x), (int) (100 * point2.x)};
		int[] zp = new int[] { (int) (100 * origin.z), (int) (100 * point1.z), (int) (100 * opposite.z), (int) (100 * point2.z)};
		
		Polygon rect = new Polygon(xp, zp, 4);
		doesIntersect = doesIntersect && rect.contains(x1*100, z1*100) && rect.contains(x2*100, z2*100);
		if (doesIntersect) {
			System.out.println("INTERSECTS");
		}
		
		return doesIntersect;
	}
	public boolean contains(Vec3d vec) {
		return super.contains(vec);
	}
	public RayTraceResult calculateIntercept(Vec3d vecA, Vec3d vecB) {
		return super.calculateIntercept(vecA, vecB);
	}
}
