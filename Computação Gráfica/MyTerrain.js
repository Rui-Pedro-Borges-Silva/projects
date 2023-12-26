import {CGFobject} from '../lib/CGF.js';
import { CGFshader } from "../lib/CGF.js";
import { MyPlane } from './MyPlane.js';

/**
* MyPlane
* @constructor
 * @param scene - Reference to MyScene object
 * @param nDivs - number of divisions in both directions of the surface
 * @param minS - minimum texture coordinate in S
 * @param maxS - maximum texture coordinate in S
 * @param minT - minimum texture coordinate in T
 * @param maxT - maximum texture coordinate in T
 * @param {Array<Object>} textures - array with 2 textures which are applied to the terrain

*/
/** Represents a plane with nrDivs divisions along both axis, with center at (0,0) */
export class MyTerrain extends CGFobject{
	constructor(scene) {
		super(scene);

		this.scene = scene;

		this.plane = new MyPlane(scene, 30);

        this.terrainShader = new CGFshader(this.scene.gl, "shaders/terrain.vert", "shaders/terrain.frag");
	}

	/**
    * Set Method for changing current textures
    */
	updateTextures(textures) {
		this.terrainTex = textures[0][0];
		this.terrainMap = textures[0][1];
	
		this.terrainShader.setUniformsValues({ terrainTex: 0 });
		this.terrainShader.setUniformsValues({ terrainMap: 1 });
	}

	display() {
		this.scene.pushMatrix();
		this.scene.setActiveShader(this.terrainShader);
		this.terrainTex.bind(0);
		this.terrainMap.bind(1);
		this.scene.translate(0,-100,0);
		this.scene.scale(400,400,400);
		this.scene.rotate(-Math.PI/2.0,1,0,0);
		this.plane.display();
		this.scene.popMatrix();
		// -- Shader is reset in MyScene -- //
	}

}


