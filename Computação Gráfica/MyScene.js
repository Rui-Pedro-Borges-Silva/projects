import { CGFscene, CGFcamera, CGFaxis, CGFappearance, CGFshader, CGFtexture } from "../lib/CGF.js";
import { MyPlane } from "./MyPlane.js";
import { MySphere } from "./MySphere.js";
import { MyPanorama } from "./MyPanorama.js";
import { MyBird } from "./MyBird.js";
import { MyTerrain } from "./MyTerrain.js";
import { MyBirdEgg } from "./MyBirdEgg.js";
import { MyNest } from "./MyNest.js";




export class MyScene extends CGFscene {
  constructor() {
    super();
  }
  init(application) {
    super.init(application);
    this.initCameras();
    this.initLights();

    this.timePrevFrame=Date.now();

    //Background color
    this.gl.clearColor(0.0, 0.0, 0.0, 1.0);
    this.gl.clearDepth(100.0);
    this.gl.enable(this.gl.DEPTH_TEST);
    this.gl.enable(this.gl.CULL_FACE);
    this.gl.depthFunc(this.gl.LEQUAL);

    this.enableTextures(true);


    this.slices = 40;  
    this.stacks = 40;

    this.aceleration = 0.5;
    this.theta = 1;


    //Initialize scene objects
    this.axis = new CGFaxis(this);
    this.plane = new MyPlane(this,30);
    this.sphere = new MySphere(this,this.slices,this.stacks);  //esfera
    this.panorama = new MyPanorama(this, new CGFtexture(this, "images/panorama4.jpg"));

    //Ovo
    this.egg = new MyBirdEgg(this, this.slices, this.stacks, 0, 0, 0);
    this.texture11 = new CGFtexture(this, "images/egg-texture.jpg");
    this.nnAppearance = new CGFappearance(this);
    this.nnAppearance.setTexture(this.texture11);
    this.nnAppearance.setTextureWrap('REPEAT', 'REPEAT');
    
    //ninho
    this.nest= new MyNest(this,15,15,false)
    this.texture10 = new CGFtexture(this, "images/nest-texture.jpg");
    this.nAppearance = new CGFappearance(this);
    this.nAppearance.setTexture(this.texture10);
    this.nAppearance.setTextureWrap('REPEAT', 'REPEAT');
    
    
    // Initialize the bird
    this.bird = new MyBird(this);

    this.bird.scaleFactor = 1;
    this.setUpdatePeriod(100);
    this.terrain = new MyTerrain(this);
    

    //Objects connected to MyInterface
    this.displayAxis = true;
    this.scaleFactor = 1;
    this.displaySphere = true;  //esfera


    //Textures
    this.texture = new CGFtexture(this, "images/terrain.jpg");  //Terreno
    this.appearance = new CGFappearance(this);
    this.appearance.setTexture(this.texture);
    this.appearance.setTextureWrap('REPEAT', 'REPEAT');
    
    this.texture2 = new CGFtexture(this, "images/earth.jpg");  //Terra
    this.appearance2 = new CGFappearance(this);
    this.appearance2.setAmbient(1.0, 1.0, 1.0, 1);
    this.appearance2.setDiffuse(1.0, 1.0, 1.0, 1.0);
    this.appearance2.setSpecular(10.0, 10.0, 10.0, 1);
    this.appearance2.setShininess(10.0);
    this.appearance2.setTexture(this.texture2);
    this.appearance2.setTextureWrap('REPEAT', 'REPEAT');



    this.initTerrainTextures();
    this.updateTerrainTextures();

  }


  initTerrainTextures() {
    this.defaultTerrainTexture = [
        new CGFtexture(this, 'images/terrain.jpg'),
        new CGFtexture(this, 'images/heightmap.jpg')
    ];

    this.terrainTextures = [this.defaultTerrainTexture];

    this.terrainTextureIds = {
        'Default': 0
    };
  }

  /**
   * Method for updating terrain's textures using the interface selected texture
   */
  updateTerrainTextures() {
    this.terrain.updateTextures(this.terrainTextures);
  }



  initLights() {
    this.lights[0].setPosition(15, 0, 5, 1);
    this.lights[0].setDiffuse(1.0, 1.0, 1.0, 1.0);
    this.lights[0].enable();
    this.lights[0].update();
  }
  initCameras() {
    this.camera = new CGFcamera(1.0, 0.1, 1000, vec3.fromValues(50, 10, 15), vec3.fromValues(0, 0, 0));
  }
  setDefaultAppearance() {
    this.setAmbient(0.2, 0.4, 0.8, 1.0);
    this.setDiffuse(0.2, 0.4, 0.8, 1.0);
    this.setSpecular(0.2, 0.4, 0.8, 1.0);
    this.setEmission(0,0,0,1);  
    this.setShininess(10.0);
  }



  update(t) {
    //console.log(this.bird.x);
    this.checkKeys();

    var dt = t - this.timePrevFrame;
    this.bird.update(t, dt);

    this.timePrevFrame = t;
  }

  getBird() {
    return this.bird;
  }

  checkKeys(){
    var text = "Keys pressed: ";
    var keysPressed=false;

    if (this.gui.isKeyPressed("KeyW")){
      text += " W ";
      this.bird.accelerate(this.aceleration);
      keysPressed = true;
    }

    if (this.gui.isKeyPressed("KeyS")){
      text += " S ";
      this.bird.accelerate(-this.aceleration);
      keysPressed = true;
    }

    if (this.gui.isKeyPressed("KeyA")){
      text += " A ";
      this.bird.turn(this.theta);
      keysPressed = true;
    }

    if (this.gui.isKeyPressed("KeyD")){
      text += " D ";
      this.bird.turn(-this.theta);
      keysPressed = true;
    }

    if (this.gui.isKeyPressed("KeyR")){
      text += " R ";
      this.bird.resetBird();
      keysPressed = true;
    }

    if (keysPressed)
      console.log(text);
  }





  display() {
    // ---- BEGIN Background, camera and axis setup
    // Clear image and depth buffer everytime we update the scene
    this.gl.viewport(0, 0, this.gl.canvas.width, this.gl.canvas.height);
    this.gl.clear(this.gl.COLOR_BUFFER_BIT | this.gl.DEPTH_BUFFER_BIT);
    // Initialize Model-View matrix as identity (no transformation
    this.updateProjectionMatrix();
    this.loadIdentity();
    // Apply transformations corresponding to the camera position relative to the origin
    this.applyViewMatrix();



    
    this.pushMatrix();
    this.translate(0, 0, 0); 
    this.scale(0.5, 0.5, 0.5);
    this.nnAppearance.apply()
    this.egg.display();
    this.popMatrix();

    //nest
    this.pushMatrix();
    this.nAppearance.apply()
    this.nest.display();
    this.popMatrix();


    //ave
    this.pushMatrix();
    this.scale(2,2,2);
    this.bird.display();
    this.popMatrix();
    // Draw axis
    if (this.displayAxis) this.axis.display();


    if (this.displaySphere){

    //panorama
    ///*
    this.pushMatrix();
    //this.appearance2.apply();
    this.panorama.display();
    this.popMatrix();
    //*/



    //mundo
    /*
    this.pushMatrix();
    this.appearance2.apply();
    this.sphere.display();
    this.popMatrix();
    */




    //ave
    ///*
    this.pushMatrix();
    this.scale(2,2,2);
    this.bird.display();
    this.popMatrix();
    //*/



    //plano
    /*
    this.pushMatrix();
    this.appearance.apply();
    this.translate(0,-100,0);
    this.scale(400,400,400);
    this.rotate(-Math.PI/2.0,1,0,0);
    this.plane.display();
    this.popMatrix();
    */



    //terreno
    ///*
    this.terrain.display();
    // -- Restore Shader -- //
    this.setActiveShader(this.defaultShader);
    //*/

    
    
    }
  }
}
