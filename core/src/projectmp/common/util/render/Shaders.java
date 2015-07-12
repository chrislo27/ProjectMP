package projectmp.common.util.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shaders {

	private Shaders() {
	}

	public static final String VERTDEFAULT = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE
			+ ";\n" + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "attribute vec2 "
			+ ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +

			"uniform mat4 u_projTrans;\n" + " \n" + "varying vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n" +

			"void main() {\n" + "	vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
			+ "	vTexCoord = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
			+ "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "}";
	
	public static final String FRAGBAKE = Gdx.files.internal("shaders/mask.frag").readString();
	
	public static final String FRAGBAKENOISE = "#ifdef GL_ES\n" + 
			"#define LOWP lowp\n" + 
			"precision mediump float;\n" + 
			"#else\n" + 
			"#define LOWP \n" + 
			"#endif\n" + 
			"varying LOWP vec4 vColor;\n" + 
			"varying vec2 vTexCoord;\n" + 
			"uniform sampler2D u_texture;\n" + 
			"uniform sampler2D u_mask;\n" + 
			"\n" + 
			"\n" + 
			"\n" + 
			"uniform float time;\n" + 
			"uniform float intensity;\n" + 
			"\n" + 
			"	/////////////////////////////////////////////////////////////////////////\n" + 
			"	/////////////////// SIMPLEX NOISE FROM WEBGL-NOISE //////////////////////\n" + 
			"	/////////////////////////////////////////////////////////////////////////\n" + 
			"	//            https://github.com/ashima/webgl-noise/wiki               //\n" + 
			"	/////////////////////////////////////////////////////////////////////////\n" + 
			"\n" + 
			"vec3 mod289(vec3 x) {\n" + 
			"  return x - floor(x * (1.0 / 289.0)) * 289.0;\n" + 
			"}\n" + 
			"\n" + 
			"vec2 mod289(vec2 x) {\n" + 
			"  return x - floor(x * (1.0 / 289.0)) * 289.0;\n" + 
			"}\n" + 
			"\n" + 
			"vec3 permute(vec3 x) {\n" + 
			"  return mod289(((x*34.0)+1.0)*x);\n" + 
			"}\n" + 
			"\n" + 
			"float snoise(vec2 v) {\n" + 
			"  const vec4 C = vec4(0.211324865405187,  // (3.0-sqrt(3.0))/6.0\n" + 
			"                      0.366025403784439,  // 0.5*(sqrt(3.0)-1.0)\n" + 
			"                     -0.577350269189626,  // -1.0 + 2.0 * C.x\n" + 
			"                      0.024390243902439); // 1.0 / 41.0\n" + 
			"// First corner\n" + 
			"  vec2 i  = floor(v + dot(v, C.yy) );\n" + 
			"  vec2 x0 = v -   i + dot(i, C.xx);\n" + 
			"\n" + 
			"// Other corners\n" + 
			"  vec2 i1;\n" + 
			"  //i1.x = step( x0.y, x0.x ); // x0.x > x0.y ? 1.0 : 0.0\n" + 
			"  //i1.y = 1.0 - i1.x;\n" + 
			"  i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);\n" + 
			"  // x0 = x0 - 0.0 + 0.0 * C.xx ;\n" + 
			"  // x1 = x0 - i1 + 1.0 * C.xx ;\n" + 
			"  // x2 = x0 - 1.0 + 2.0 * C.xx ;\n" + 
			"  vec4 x12 = x0.xyxy + C.xxzz;\n" + 
			"  x12.xy -= i1;\n" + 
			"\n" + 
			"// Permutations\n" + 
			"  i = mod289(i); // Avoid truncation effects in permutation\n" + 
			"  vec3 p = permute( permute( i.y + vec3(0.0, i1.y, 1.0 ))\n" + 
			"		+ i.x + vec3(0.0, i1.x, 1.0 ));\n" + 
			"\n" + 
			"  vec3 m = max(0.5 - vec3(dot(x0,x0), dot(x12.xy,x12.xy), dot(x12.zw,x12.zw)), 0.0);\n" + 
			"  m = m*m ;\n" + 
			"  m = m*m ;\n" + 
			"\n" + 
			"// Gradients: 41 points uniformly over a line, mapped onto a diamond.\n" + 
			"// The ring size 17*17 = 289 is close to a multiple of 41 (41*7 = 287)\n" + 
			"\n" + 
			"  vec3 x = 2.0 * fract(p * C.www) - 1.0;\n" + 
			"  vec3 h = abs(x) - 0.5;\n" + 
			"  vec3 ox = floor(x + 0.5);\n" + 
			"  vec3 a0 = x - ox;\n" + 
			"\n" + 
			"// Normalise gradients implicitly by scaling m\n" + 
			"// Approximation of: m *= inversesqrt( a0*a0 + h*h );\n" + 
			"  m *= 1.79284291400159 - 0.85373472095314 * ( a0*a0 + h*h );\n" + 
			"\n" + 
			"// Compute final noise value at P\n" + 
			"  vec3 g;\n" + 
			"  g.x  = a0.x  * x0.x  + h.x  * x0.y;\n" + 
			"  g.yz = a0.yz * x12.xz + h.yz * x12.yw;\n" + 
			"  return 130.0 * dot(m, g);\n" + 
			"}\n" + 
			"\n" + 
			"\n" + 
			"	/////////////////////////////////////////////////////////////////////////\n" + 
			"	////////////////////       END SIMPLEX NOISE     ////////////////////////\n" + 
			"	/////////////////////////////////////////////////////////////////////////\n" + 
			"	\n" + 
			"	\n" + 
			"void main(void) {\n" + 
			"	//sample the colour from the first texture\n" + 
			"	vec4 texColor0 = texture2D(u_texture, vTexCoord);\n" + 
			"	\n" + 
			"	//pertube texcoord by x and y\n" + 
			"	vec2 distort = (intensity / 2.5) * vec2(snoise(vTexCoord + vec2(0.0, time/3.0)),\n" + 
			"                              snoise(vTexCoord + vec2(time/3.0, 0.0)) );\n" + 
			"	\n" + 
			"	//get the mask; we will only use the alpha channel\n" + 
			"	float mask = texture2D(u_mask, vTexCoord + distort).a;\n" + 
			"\n" + 
			"	//interpolate the alpha of the first texture by the mask alpha \n" + 
			"	gl_FragColor = vColor * mix(texColor0, vec4(texColor0.rgb, 0.0), mask);\n" + 
			"}";

	public static final String VERTBLUEPRINT = "attribute vec4 a_position;\r\n"
			+ "attribute vec4 a_color;\r\n" + "attribute vec2 a_texCoord0;\r\n" + "\r\n"
			+ "uniform mat4 u_projTrans;\r\n" + "\r\n" + "varying vec4 v_color;\r\n"
			+ "varying vec2 v_texCoords;\r\n" + "\r\n" + "void main() {\r\n"
			+ "    v_color = a_color;\r\n" + "    v_texCoords = a_texCoord0;\r\n"
			+ "    gl_Position = u_projTrans * a_position;\r\n" + "}";

	public static final String FRAGBLUEPRINT = "#ifdef GL_ES\r\n"
			+ "    precision mediump float;\r\n" + "#endif\r\n" + "\r\n"
			+ "varying vec4 v_color;\r\n" + "varying vec2 v_texCoords;\r\n"
			+ "uniform sampler2D u_texture;\r\n" + "uniform mat4 u_projTrans;\r\n" + "\r\n"
			+ "void main() {\r\n"
			+ "        vec3 color = texture2D(u_texture, v_texCoords).rgb;\r\n"
			+ "        float gray = (color.r + color.g + color.b) / 3.0;\r\n"
			+ "        vec3 grayscale = vec3(gray);\r\n"
			+ "		 grayscale.b = grayscale.b + 0.25;\r\n"
			+ "        gl_FragColor = vec4(grayscale, texture2D(u_texture, v_texCoords).a);\r\n"
			+ "}";

	public static final String FRAGBLUEPRINT2 =
	// GL ES specific stuff
	"#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n#version 120"
			+ //
			"varying LOWP vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n"
			+ "uniform sampler2D u_texture;\n"
			+ "void main() {\n"
			+ "       vec4 texColor = texture2D(u_texture, vTexCoord);\n"
			+ "       \n"
			+ "if(texColor.r < 0.019608 || texColor.g < 0.019608 || texColor.b < 0.019608){ \n"
			+ "       gl_FragColor = vec4(0.0, 0.0, 0.0, texColor.a);}\n"
			+ "       gl_FragColor = vec4(0.019607, 0.015686, 0.564705, vColor.a);\n" + "}";

	public static final String VERTTOON = "#ifdef GL_ES\r\n" + "#define MED mediump\r\n"
			+ "#else\r\n" + "#define MED\r\n" + "#endif\r\n" + " \r\n"
			+ "attribute vec4 a_position;\r\n" + "attribute vec2 a_texCoord0;\r\n"
			+ "varying MED vec2 v_texCoord0;\r\n" + " \r\n" + "void main(){\r\n"
			+ "    v_texCoord0 = a_texCoord0;\r\n" + "    gl_Position = a_position;\r\n" + "}";

	public static final String FRAGTOON = "#ifdef GL_ES\r\n" + "#define LOWP lowp\r\n"
			+ "#define MED mediump\r\n" + "precision lowp float;\r\n" + "#else\r\n"
			+ "#define LOWP\r\n" + "#define MED\r\n" + "#endif\r\n" + " \r\n"
			+ "uniform sampler2D u_texture;\r\n" + "varying MED vec2 v_texCoord0;\r\n" + " \r\n"
			+ "float toonify(in float intensity) {\r\n" + "    if (intensity > 0.8)\r\n"
			+ "        return 1.0;\r\n" + "    else if (intensity > 0.5)\r\n"
			+ "        return 0.8;\r\n" + "    else if (intensity > 0.25)\r\n"
			+ "        return 0.3;\r\n" + "    else\r\n" + "        return 0.1;\r\n" + "}\r\n"
			+ " \r\n" + "void main(){\r\n"
			+ "    vec4 color = texture2D(u_texture, v_texCoord0);\r\n"
			+ "    float factor = toonify(max(color.r, max(color.g, color.b)));\r\n"
			+ "    gl_FragColor = vec4(factor*color.rgb, color.a);\r\n" + "}";

	public static final String VERTGREY = "attribute vec4 a_position;\r\n"
			+ "attribute vec4 a_color;\r\n" + "attribute vec2 a_texCoord0;\r\n" + "\r\n"
			+ "uniform mat4 u_projTrans;\r\n" + "\r\n" + "varying vec4 v_color;\r\n"
			+ "varying vec2 v_texCoords;\r\n" + "\r\n" + "void main() {\r\n"
			+ "    v_color = a_color;\r\n" + "    v_texCoords = a_texCoord0;\r\n"
			+ "    gl_Position = u_projTrans * a_position;\r\n" + "}";

	public static final String FRAGGREY = "#ifdef GL_ES\r\n" + "    precision mediump float;\r\n"
			+ "#endif\r\n" + "\r\n" + "varying vec4 v_color;\r\n" + "varying vec2 v_texCoords;\r\n"
			+ "uniform sampler2D u_texture;\r\n"
			+ "uniform mat4 u_projTrans;\r\n uniform float intensity;" + "\r\n"
			+ "void main() {\r\n" + "        vec4 color = texture2D(u_texture, v_texCoords);\r\n"
			+ "        float gray = (color.r + color.g + color.b) / 3.0;\r\n"
			+ "        vec3 grayscale = vec3(gray, gray, gray);\r\n"
			+ "		 vec3 finalcolor = mix(v_color.rgb, grayscale, intensity);\r\n"
			+ "        gl_FragColor = vec4(color.rgb, intensity);\r\n" + "}";

	public static final String VERTBLUR = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE
			+ ";\n" + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "attribute vec2 "
			+ ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +

			"uniform mat4 u_projTrans;\n" + " \n" + "varying vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n" +

			"void main() {\n" + "	vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
			+ "	vTexCoord = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
			+ "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "}";

	public static final String FRAGBLUR = "#ifdef GL_ES\n"
			+ "#define LOWP lowp\n"
			+ "precision mediump float;\n"
			+ "#else\n"
			+ "#define LOWP \n"
			+ "#endif\n"
			+ "varying LOWP vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n"
			+ "\n"
			+ "uniform sampler2D u_texture;\n"
			+ "uniform float resolution;\n"
			+ "uniform float radius;\n"
			+ "uniform vec2 dir;\n"
			+ "\n"
			+ "void main() {\n"
			+ "	vec4 sum = vec4(0.0);\n"
			+ "	vec2 tc = vTexCoord;\n"
			+ "	float blur = radius/resolution; \n"
			+ "    \n"
			+ "    float hstep = dir.x;\n"
			+ "    float vstep = dir.y;\n"
			+ "    \n"
			+ "	sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.05;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.09;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.12;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.15;\n"
			+ "	\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.16;\n"
			+ "	\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.15;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.12;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.09;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.05;\n"
			+ "\n" + "	gl_FragColor = vColor * vec4(sum.rgb, 1.0);\n" + "}";

	public static final String VERTINVERT = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE
			+ ";\n" + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "attribute vec2 "
			+ ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +

			"uniform mat4 u_projTrans;\n" + " \n" + "varying vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n" +

			"void main() {\n" + "	vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
			+ "	vTexCoord = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
			+ "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "}";
	public static final String FRAGINVERT = "#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n"
			+ //
			"varying LOWP vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n"
			+ "uniform sampler2D u_texture;\n"
			+ "void main() {\n"
			+ "	vec4 texColor = texture2D(u_texture, vTexCoord);\n"
			+ "	\n"
			+ "	texColor.rgb = 1.0 - texColor.rgb;\n"
			+ "	\n"
			+ "	gl_FragColor = texColor * vColor;\n" + "}";

	public static final String VERTWARP = VERTDEFAULT;

	public static final String FRAGWARP = Gdx.files.internal("shaders/warp.frag").readString();

	public static final String VERTSWIZZLE = VERTDEFAULT;

	public static final String FRAGSWIZZLE = Gdx.files.internal("shaders/swizzle.frag")
			.readString();

	public static final String VERTDISTANCEFIELD = Gdx.files.internal("shaders/distancefield.vert")
			.readString();

	public static final String FRAGMESH = Gdx.files.internal("shaders/mesh.frag").readString();

	public static final String VERTMESH = Gdx.files.internal("shaders/mesh.vert").readString();

	public static final String FRAGDISTANCEFIELD = Gdx.files.internal("shaders/distancefield.frag")
			.readString();

}
