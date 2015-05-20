//"in" attributes from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;


//our different texture units
uniform sampler2D u_texture; //default GL_TEXTURE0, expected by SpriteBatch
uniform sampler2D u_mask;

void main(void) {
    vec4 sentIn = texture2D(u_texture, vTexCoord);

    // get the mask; we will only use the alpha channel
    float mask = texture2D(u_mask, vTexCoord).a;

    gl_FragColor = vColor * mix(sentIn, vec4(0.0, 0.0, 0.0, 0.0), 1.0 - mask);
}