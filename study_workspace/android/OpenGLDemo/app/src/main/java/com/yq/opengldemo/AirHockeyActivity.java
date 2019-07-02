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

        private static final int BYTES_PER_FLOAT = 4;
        //x and y for a point for tableVerticesWithTrangles
        private static final int POSITION_COMPONENT_COUNT = 2;
        private static final int COLOR_COMPONENT_COUNT = 3;
        /*The stride tells OpenGL the interval between each position or each color.
        As we now have both a position and a color attribute in the same data array, OpenGL can no
        longer assume that the next position follows immediately after the previous position.
        Once OpenGL has read the position for a vertex, it will have to skip over the color for
        the current vertex if it wants to read the position for the next vertex. We’ll use the
        stride to tell OpenGL how many bytes are between each position so that it knows how far it has to skip. */
        private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

        private static final String A_COLOR = "a_Color";
        private static final String A_POSITION = "a_Position";

        private int aColorLocation;

        private int aPositionLocation;


        private final FloatBuffer vertexData;

        private int program;

        private Context mContext;

        public AirHockeyRenderer(Context context) {
            mContext = context;

            float[] tableVerticesWithTrangles = {
                //order of coordinates: x, y, r, g, b

                // Triangle Fan
                0,      0,      1f,     1f,     1f
                -0.5f,  -0.5f,  0.7f,   0.7f,   0.7f,
                0.5f,   -0.5f,  0.7f,   0.7f,   0.7f,
                0.5f,   0.5f,   0.7f,   0.7f,   0.7f,
                -0.5f,  0.5f,   0.7f,   0.7f,   0.7f,
                -0.5f,  -0.5f,  0.7f,   0.7f,   0.7f,

                //Line 1
                -0.5f,  0f,     1f,     0f,     0f,
                0.5f,   0f,     1f,     0f,     0f,

                //Mallets
                0f,     -0.25f, 0f,     0f,     1f,
                0f,     0.25f,  1f,     0f,     0f,
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

            aColorLocation = GLES20.glGetUniformLocation(program, A_COLOR);
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
                //applies when we store more than one attribute in a single array.
                STRIDE,
                //This tells OpenGL where to read the data. Don’t forget that it will start reading from the buffer’s current position,
                // so if we hadn’t called vertexData.position(0), it would probably try to read past the end of the buffer and crash our application.
                vertexData);

            //now knows where to find all the data it needs
            GLES20.glEnableVertexAttribArray(aPositionLocation);


            //when OpenGL starts reading in the color attributes, we want it to start at the first color attribute, not the first position attribute.
            vertexData.position(POSITION_COMPONENT_COUNT);
            GLES20.glVertexAttribPointer(
                aColorLocation,
                COLOR_COMPONENT_COUNT,
                GLES20.GL_FLOAT,
                false,
                /*The stride tells OpenGL how many bytes are between each color, so that when it
                reads in the colors for all of the vertices, it knows how many bytes it needs to
                skip to read the color for the next vertex.*/
                STRIDE,
                vertexData);
            GLES20.glEnableVertexAttribArray(aColorLocation);
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
            //  delete:Since we’ve already associated our vertex data with a_Color, all we need to do is call glDrawArrays(), and OpenGL will automatically read in the color attributes from our vertex data.
//            GLES20.glDrawArrays(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
            /*The second argument tells OpenGL to read in vertices starting at the beginning of our vertex array,
            and the third argument tells OpenGL to read in six vertices. Since there are three vertices per triangle,
            this call will end up drawing two triangles.
            read info from tableVerticesWithTriangles*/
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

            //draw dividing line, read info from tableVerticesWithTriangles
//            GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

            //draw mallets
            //first
//            GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);
            //second
//            GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
        }
    }
}
