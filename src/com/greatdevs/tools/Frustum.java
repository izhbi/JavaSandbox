package com.greatdevs.tools;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

public class Frustum {
	FloatBuffer modelBuffer;
	FloatBuffer projectionBuffer;

	private float[][] frustum = new float[6][4]; // Holds The Current Frustum
													// Plane Equations

	public Frustum(){
		modelBuffer = BufferUtils.createFloatBuffer(16);
		projectionBuffer = BufferUtils.createFloatBuffer(16);
	}
	
	public void calculateFrustum(Matrix4f projectionMatrixHandler, Matrix4f modelMatrixHandler) {
		modelBuffer.clear();
		projectionBuffer.clear();
		float[] clip = new float[16];
		float t;

		float[] proj = new float[16];
		float[] modl = new float[16];
		projectionMatrixHandler.store(projectionBuffer);
		modelMatrixHandler.store(modelBuffer);
		projectionBuffer.flip();
		modelBuffer.flip();
		projectionBuffer.get(proj);
		modelBuffer.get(modl);

		// Concatenate (Multiply) The Two Matricies
		clip[0] = modl[0] * proj[0] + modl[1] * proj[4] + modl[2] * proj[8]
				+ modl[3] * proj[12];
		clip[1] = modl[0] * proj[1] + modl[1] * proj[5] + modl[2] * proj[9]
				+ modl[3] * proj[13];
		clip[2] = modl[0] * proj[2] + modl[1] * proj[6] + modl[2] * proj[10]
				+ modl[3] * proj[14];
		clip[3] = modl[0] * proj[3] + modl[1] * proj[7] + modl[2] * proj[11]
				+ modl[3] * proj[15];

		clip[4] = modl[4] * proj[0] + modl[5] * proj[4] + modl[6] * proj[8]
				+ modl[7] * proj[12];
		clip[5] = modl[4] * proj[1] + modl[5] * proj[5] + modl[6] * proj[9]
				+ modl[7] * proj[13];
		clip[6] = modl[4] * proj[2] + modl[5] * proj[6] + modl[6] * proj[10]
				+ modl[7] * proj[14];
		clip[7] = modl[4] * proj[3] + modl[5] * proj[7] + modl[6] * proj[11]
				+ modl[7] * proj[15];

		clip[8] = modl[8] * proj[0] + modl[9] * proj[4] + modl[10] * proj[8]
				+ modl[11] * proj[12];
		clip[9] = modl[8] * proj[1] + modl[9] * proj[5] + modl[10] * proj[9]
				+ modl[11] * proj[13];
		clip[10] = modl[8] * proj[2] + modl[9] * proj[6] + modl[10] * proj[10]
				+ modl[11] * proj[14];
		clip[11] = modl[8] * proj[3] + modl[9] * proj[7] + modl[10] * proj[11]
				+ modl[11] * proj[15];

		clip[12] = modl[12] * proj[0] + modl[13] * proj[4] + modl[14] * proj[8]
				+ modl[15] * proj[12];
		clip[13] = modl[12] * proj[1] + modl[13] * proj[5] + modl[14] * proj[9]
				+ modl[15] * proj[13];
		clip[14] = modl[12] * proj[2] + modl[13] * proj[6] + modl[14]
				* proj[10] + modl[15] * proj[14];
		clip[15] = modl[12] * proj[3] + modl[13] * proj[7] + modl[14]
				* proj[11] + modl[15] * proj[15];

		// Extract the RIGHT clipping plane
		frustum[0][0] = clip[3] - clip[0];
		frustum[0][1] = clip[7] - clip[4];
		frustum[0][2] = clip[11] - clip[8];
		frustum[0][3] = clip[15] - clip[12];

		// Normalize it
		t = (float) Math.sqrt(frustum[0][0] * frustum[0][0] + frustum[0][1]
				* frustum[0][1] + frustum[0][2] * frustum[0][2]);
		frustum[0][0] /= t;
		frustum[0][1] /= t;
		frustum[0][2] /= t;
		frustum[0][3] /= t;

		// Extract the LEFT clipping plane
		frustum[1][0] = clip[3] + clip[0];
		frustum[1][1] = clip[7] + clip[4];
		frustum[1][2] = clip[11] + clip[8];
		frustum[1][3] = clip[15] + clip[12];

		// Normalize it
		t = (float) Math.sqrt(frustum[1][0] * frustum[1][0] + frustum[1][1]
				* frustum[1][1] + frustum[1][2] * frustum[1][2]);
		frustum[1][0] /= t;
		frustum[1][1] /= t;
		frustum[1][2] /= t;
		frustum[1][3] /= t;

		// Extract the BOTTOM clipping plane
		frustum[2][0] = clip[3] + clip[1];
		frustum[2][1] = clip[7] + clip[5];
		frustum[2][2] = clip[11] + clip[9];
		frustum[2][3] = clip[15] + clip[13];
		//System.out.println(frustum[2][3]);
		// Normalize it
		t = (float) Math.sqrt(frustum[2][0] * frustum[2][0] + frustum[2][1]
				* frustum[2][1] + frustum[2][2] * frustum[2][2]);
		//System.out.println(t);
		frustum[2][0] /= t;
		frustum[2][1] /= t;
		frustum[2][2] /= t;
		frustum[2][3] /= t;

		// Extract the TOP clipping plane
		frustum[3][0] = clip[3] - clip[1];
		frustum[3][1] = clip[7] - clip[5];
		frustum[3][2] = clip[11] - clip[9];
		frustum[3][3] = clip[15] - clip[13];

		// Normalize it
		t = (float) Math.sqrt(frustum[3][0] * frustum[3][0] + frustum[3][1]
				* frustum[3][1] + frustum[3][2] * frustum[3][2]);
		frustum[3][0] /= t;
		frustum[3][1] /= t;
		frustum[3][2] /= t;
		frustum[3][3] /= t;

		// Extract the FAR clipping plane
		frustum[4][0] = clip[3] - clip[2];
		frustum[4][1] = clip[7] - clip[6];
		frustum[4][2] = clip[11] - clip[10];
		frustum[4][3] = clip[15] - clip[14];

		// Normalize it
		t = (float) Math.sqrt(frustum[4][0] * frustum[4][0] + frustum[4][1]
				* frustum[4][1] + frustum[4][2] * frustum[4][2]);
		frustum[4][0] /= t;
		frustum[4][1] /= t;
		frustum[4][2] /= t;
		frustum[4][3] /= t;

		// Extract the NEAR clipping plane. This is last on purpose (see
		// pointinfrustum() for reason)
		frustum[5][0] = clip[3] + clip[2];
		frustum[5][1] = clip[7] + clip[6];
		frustum[5][2] = clip[11] + clip[10];
		frustum[5][3] = clip[15] + clip[14];

		// Normalize it
		t = (float) Math.sqrt(frustum[5][0] * frustum[5][0] + frustum[5][1]
				* frustum[5][1] + frustum[5][2] * frustum[5][2]);
		frustum[5][0] /= t;
		frustum[5][1] /= t;
		frustum[5][2] /= t;
		frustum[5][3] /= t;

		/*System.out.println("===============================================");
		for (int p = 0; p < 6; p++) {
			System.out.println(p + " A : " + frustum[p][0] + " B : "
					+ frustum[p][1] + " C : " + frustum[p][2] + " D : "
					+ frustum[p][3]);
		}*/
	}

	// Test If A Point Is In The Frustum.
	public boolean pointInFrustum(float x, float y, float z) {
		int p;

		for (p = 0; p < 6; p++)
			if (frustum[p][0] * x + frustum[p][1] * y + frustum[p][2] * z
					+ frustum[p][3] <= 0)
				return false;
		return true;
	}

	// Test If A Sphere Is In The Frustum
	public boolean sphereInFrustum(float x, float y, float z, float radius) {
		int p;

		for (p = 0; p < 6; p++)
			if (frustum[p][0] * x + frustum[p][1] * y + frustum[p][2] * z
					+ frustum[p][3] <= -radius)
				return false;
		return true;
	}

	// Test If A Cube Is In The Frustum
	public boolean cubeInFrustum(float x, float y, float z, float sizeX, float sizeY, float sizeZ) {
		int p;

		for (p = 0; p < 6; p++) {
			if (frustum[p][0] * (x - sizeX) + frustum[p][1] * (y - sizeY) + frustum[p][2] * (z - sizeZ) + frustum[p][3] > 0)
				continue;
			if (frustum[p][0] * (x + sizeX) + frustum[p][1] * (y - sizeY) + frustum[p][2] * (z - sizeZ) + frustum[p][3] > 0)
				continue;
			if (frustum[p][0] * (x - sizeX) + frustum[p][1] * (y + sizeY) + frustum[p][2] * (z - sizeZ) + frustum[p][3] > 0)
				continue;
			if (frustum[p][0] * (x + sizeX) + frustum[p][1] * (y + sizeY) + frustum[p][2] * (z - sizeZ) + frustum[p][3] > 0)
				continue;
			if (frustum[p][0] * (x - sizeX) + frustum[p][1] * (y - sizeY) + frustum[p][2] * (z + sizeZ) + frustum[p][3] > 0)
				continue;
			if (frustum[p][0] * (x + sizeX) + frustum[p][1] * (y - sizeY) + frustum[p][2] * (z + sizeZ) + frustum[p][3] > 0)
				continue;
			if (frustum[p][0] * (x - sizeX) + frustum[p][1] * (y + sizeY) + frustum[p][2] * (z + sizeZ) + frustum[p][3] > 0)
				continue;
			if (frustum[p][0] * (x + sizeX) + frustum[p][1] * (y + sizeY) + frustum[p][2] * (z + sizeZ) + frustum[p][3] > 0)
				continue;
			return false;
		}
		return true;
	}
}
