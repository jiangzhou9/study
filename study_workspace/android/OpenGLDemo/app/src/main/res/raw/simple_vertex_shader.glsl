//This vertex shader will be called once for every single vertex that we’ve defined.


//When it’s called, it will receive the current vertex’s position in the a_Position attribute, which is defined to be a vec4.
//vec4: xyzw(default:0001)
attribute vec4 a_Position;

//All it does is copy the position that we’ve defined to the special output variable gl_Position
void main()
{
    gl_Position = a_Position;
    //mallets size, tell OpenGL how large the points should appear on the screen
    gl_PointSize = 10.0;
}