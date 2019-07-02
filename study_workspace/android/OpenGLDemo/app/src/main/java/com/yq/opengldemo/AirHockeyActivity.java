package com.yq.opengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yq.opengldemo.util.L;
import com.yq.opengldemo.util.ShaderHelper;
import com.yq.opengldemo.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AirHockeyActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_hockey);

        mGLSurfaceView = findViewById(R.id.glsv);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new AirHockeyRenderer(this));
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private static class AirHockeyRenderer implements GLSurfaceView.Renderer {

        //x and y for a point for tableVerticesWithTrangles
        private static final int POSITION_COMPONENT_COUNT = 2;

        private static final String U_COLOR = "u_Color";
        private static final String A_POSITION = "a_Position";

        //We’ll use that when we want to update the value of this uniform later on.
        private int uColorLocation;

        private int aPositionLocation;

        private static final int BYTES_PER_FLOAT = 4;
        private final FloatBuffer vertexData;

        private int program;

        private Context mContext;

        public AirHockeyRenderer(Context context) {
            mContext = context;

            float[] tableVerticesWithTrangles = {
                //triangle 1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,
                //triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,

                //Line 1
                -0.5f, 0f,
                0.5f, 0f,

                //Mallets
                0f, -0.25f,
                0f, 0.25f
            };

            vertexData = ByteBuffer
                //allocated a block of native memory,this memory will not be managed by the garbage collector
                .allocateDirect(tableVerticesWithTrangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
            //copy data from Dalvik’s memory to native memory
            vertexData.put(tableVerticesWithTrangles);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            L.e("onSurfaceCreated");
            //设置清空屏幕用的颜色,rgba
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

            String vertexShaderSouce = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
            String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);

            int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSouce);
            int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

            //When OpenGL links our shaders into a program, it will actually associate each uniform defined in the vertex shader with a location number.
            program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
            ShaderHelper.validataProgram(program);

            GLES20.glUseProgram(program);

            //A uniform’s location is unique to a program object: even if we had the same uniform name in two different programs,
            // that doesn’t mean that they’ll share the same location
            uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
            aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

            //tell OpenGL where to find data for our attribute a_Position.
            //make sure that it’ll read our data starting at the beginning and not at the middle or the end.
            vertexData.position(0);
            //tell OpenGL that it can find the data for a_Position in the buffer vertexData
            //Passing incorrect arguments to glVertexAttribPointer() can lead to strange results and can even cause the program to crash. These kinds of crashes can be hard to trace
            GLES20.glVertexAttribPointer(
                aPositionLocation,
                /* Note that we’re only passing two components per vertex:(POSITION_COMPONENT_COUNT), but in the shader,
                    a_Position is defined as a vec4, which has four components. If a component is not specified,
                    OpenGL will set the first three components to 0 and the last component to 1 by default. */
                POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT,
                //This only applies if we use integer data, so we can safely ignore it for now.
                false,
                //applies when we store more than one attribute in a single array. We only have one attribute in this chapter, so we can ignore this and pass in 0 for now.
                0,
                //This tells OpenGL where to read the data. Don’t forget that it will start reading from the buffer’s current position,
                // so if we hadn’t called vertexData.position(0), it would probably try to read past the end of the buffer and crash our application.
                vertexData);

            //now knows where to find all the data it needs
            GLES20.glEnableVertexAttribArray(aPositionLocation);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            L.e("onSurfaceChanged");
            //opengl可以渲染的surface的大小
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            L.e("onDrawFrame");
            //擦除屏幕所有颜色，并用glClearColor设置的颜色填充屏幕
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            //draw table
            /*update the value of u_Color in our shader code. Unlike attributes,
            uniforms don’t have default components, so if a uniform is defined as a vec4 in our shader,
            we need to provide all four components.*/
            GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
            /*The second argument tells OpenGL to read in vertices starting at the beginning of our vertex array,
            and the third argument tells OpenGL to read in six vertices. Since there are three vertices per triangle,
            this call will end up drawing two triangles.
            read info from tableVerticesWithTriangles*/
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

            //draw dividing line, read info from tableVerticesWithTriangles
            GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

            //draw mallets
            //first
            GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);
            //second
            GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
        }
    }
}
