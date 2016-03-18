package mathiasap.cardboard.rendering;



import android.opengl.GLES30;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import mathiasap.cardboard.matrix.Matrices;
import mathiasap.cardboard.mesh.MeshInstance;
import mathiasap.cardboard.mesh.TexMesh;
import mathiasap.cardboard.shaders.MeshShader;

public class RenderMesh {
	private MeshShader shader;
	private Map<TexMesh, List<MeshInstance>> meshInstances;
	
	public RenderMesh(MeshShader meshShader,Map<TexMesh, List<MeshInstance>> meshInstances)
	{
		shader=meshShader;
		this.meshInstances=meshInstances;
		GLES30.glEnable(GLES30.GL_CULL_FACE);
		GLES30.glCullFace(GLES30.GL_BACK);
		
		
	}

	public void draw()
	{
		for(TexMesh mesh:meshInstances.keySet()) {
			prepareMesh(mesh);
			List<MeshInstance> group = meshInstances.get(mesh);
			for (MeshInstance meshInstance : group) {
				prepareIns(meshInstance);
				GLES30.glDrawElements(GLES30.GL_TRIANGLES, meshInstance.getMesh().getMesh().getVertices(), GLES30.GL_UNSIGNED_INT, 0);
			}
			unbindMesh();

		}
		
		
	}
	private void prepareMesh(TexMesh mesh)
	{
		GLES30.glBindVertexArray(mesh.getMesh().getVAO());
		GLES30.glEnableVertexAttribArray(0);
		GLES30.glEnableVertexAttribArray(1);
		GLES30.glEnableVertexAttribArray(2);
		shader.uploadSpecular(mesh.getTex().getShine(),mesh.getTex().getReflectivity());
		GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,mesh.getTex().getTexId());
		
	}
	private void prepareIns(MeshInstance ins)
	{
		float[] transformation = Matrices.transformationMatrix(ins.getPosition(), ins.getRotX(), ins.getRotY(), ins.getRotZ(), ins.getScale());
		shader.loadTranformationMatrix(transformation);	
	}
	private void unbindMesh()
	{
		GLES30.glDisableVertexAttribArray(0);
		GLES30.glDisableVertexAttribArray(1);
		GLES30.glDisableVertexAttribArray(2);
		GLES30.glBindVertexArray(0);

	}
	

}
