package Model;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class VboBilboard extends Model {
	int vbo_vertex_handle;
	int vbo_color_handle;
	int vbo_normal_handle;
	int vbo_texture_handle;
	
	int vertices = 6;
	int vertex_size = 3; // X, Y, Z,
	int color_size = 3; // R, G, B,
	int normal_size = 3; // X, Y, Z,
	int texture_size = 2;
	
	@Override
	public void load() {


		FloatBuffer vertex_data = BufferUtils.createFloatBuffer(vertices * vertex_size);
		float v1[] = { -1f, -1f, 0f, };
		float v2[] = { 1f, -1f, 0f, };
		float v3[] = { 1f,  1f, 0f, };
		float v4[] = { -1f,  1f, 0f, };
		
		//cor
		float c1[] = { 1f, 0f, 0f, };
		float c2[] = { 0f, 1f, 0f, };
		float c3[] = { 0f,  0f,  1f, };
		float c4[] = { 1f,  0f,  1f, };
		
		//Normal
		float n1[] = { 0f, 0f, 1f, };
		float n2[] = { 0f, 0f, 1f, };
		float n3[] = { 0f, 0f, 1f, };
		float n4[] = { 0f, 0f, 1f, };
		
		//uv
		float uv1[] = { 0f, 0f};
		float uv2[] = { 1f, 0f};
		float uv3[] = { 1f, 1f};
		float uv4[] = { 0f, 1f};
			
		
		//front
		vertex_data.put(v1);
		vertex_data.put(v2);
		vertex_data.put(v3);
		
		vertex_data.put(v3);
		vertex_data.put(v4);
		vertex_data.put(v1);
		
		vertex_data.flip();

		FloatBuffer color_data = BufferUtils.createFloatBuffer(vertices * color_size);
		//front
		color_data.put(c1);
		color_data.put(c2);
		color_data.put(c3);
		
		color_data.put(c3);
		color_data.put(c4);
		color_data.put(c1);
		
		color_data.flip();
		
		FloatBuffer texture_data = BufferUtils.createFloatBuffer(vertices * texture_size);
		//front
		texture_data.put(uv1);
		texture_data.put(uv2);
		texture_data.put(uv3);
		
		texture_data.put(uv3);
		texture_data.put(uv4);
		texture_data.put(uv1);
		
		
		texture_data.flip();	
		
		FloatBuffer normal_data = BufferUtils.createFloatBuffer(vertices * normal_size);
		normal_data.put(c1);
		normal_data.put(c2);
		normal_data.put(c3);
		
		normal_data.put(c3);
		normal_data.put(c4);
		normal_data.put(c1);
		
		normal_data.flip();

		vbo_vertex_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_vertex_handle);
		glBufferData(GL_ARRAY_BUFFER, vertex_data, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vbo_color_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_color_handle);
		glBufferData(GL_ARRAY_BUFFER, color_data, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		vbo_texture_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_texture_handle);
		glBufferData(GL_ARRAY_BUFFER, texture_data, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		vbo_normal_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_normal_handle);
		glBufferData(GL_ARRAY_BUFFER, normal_data, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void draw() {
		glBindBuffer(GL_ARRAY_BUFFER, vbo_vertex_handle);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0,vertex_size,GL_FLOAT,false,0,0);

		glBindBuffer(GL_ARRAY_BUFFER, vbo_color_handle);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1,color_size,GL_FLOAT,false,0,0);
		//glColorPointer(color_size, GL_FLOAT, 0, 0l);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo_normal_handle);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2,normal_size,GL_FLOAT,false,0,0);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo_texture_handle);
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3,texture_size,GL_FLOAT,false,0,0);
		//glColorPointer(color_size, GL_FLOAT, 0, 0l);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);

		glDrawArrays(GL_TRIANGLES, 0, vertices);
		



		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
	}
}
