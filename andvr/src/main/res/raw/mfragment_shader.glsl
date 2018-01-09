//FRAGMENT SHADER

precision mediump float;       	// Set the default precision to medium. We don't need as high of a
								// precision in the fragment shader.
uniform vec3 u_LightPos;       	// The position of the light in eye space.
uniform sampler2D u_Texture;    // The input texture.

varying vec3 v_Position;		// Interpolated position for this fragment.
varying vec3 v_Normal;         	// Interpolated normal for this fragment.
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.


// The entry point for our fragment shader.
void main()
{

	// Will be used for attenuation.
//    float distance = length(u_LightPos - v_Position)+0.0001;

	// Get a lighting direction vector from the light to the vertex.
    vec3 lightVector = normalize(u_LightPos - v_Position);

	// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
	// pointing in the same direction then it will get max illumination.
    float diffuse = max(dot(v_Normal, lightVector), 0.0);

	// Add attenuation.
//    diffuse = diffuse;//* (1.0 / (distance/(v_lightPower+0.0001)));

    //specular light
    float specularCoefficient = pow(max(0.0, dot(v_Position, reflect(lightVector, v_Normal))), 0.95);

    float ambientLight = 0.2;
    float lightPower = (specularCoefficient/25.0) + (diffuse + 0.4);

	// Multiply the color by the diffuse illumination level and texture value to get final output color.
    gl_FragColor = (lightPower * texture2D(u_Texture, v_TexCoordinate));
    gl_FragColor[3] = 1.0;

  }