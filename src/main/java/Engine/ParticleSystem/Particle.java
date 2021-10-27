package Engine.ParticleSystem;

import Engine.Color;
import Engine.Math.Transform;
import Engine.Math.Vector.Vector3;
import Engine.Time;

public class Particle {

    public Transform transform;
    public Color color;
    private boolean applyGravity;

    private float lifetime;
    private float timeToLive;
    private ParticleBatch batch;

    private Vector3 velocity;
    private float terminalVelocity = 15f;

    public Particle(Transform transform, ParticleBatch batch, float lifespan, Color color, boolean applyGravity, Vector3 initialVelocity) {
        this.transform = transform;
        this.batch = batch;
        this.lifetime = lifespan;
        this.color = color;
        this.applyGravity = applyGravity;
        this.velocity = new Vector3(initialVelocity.x, initialVelocity.y, initialVelocity.z);

        timeToLive = this.lifetime;
    }

    public void Update() {
        transform.position = Vector3.Add(transform.position, Vector3.Multiply(velocity, new Vector3(Time.deltaTime, Time.deltaTime, Time.deltaTime)));

        if (applyGravity) {
            if (velocity.y > -terminalVelocity) {
                velocity.y -= Time.deltaTime;
            }
        }

        timeToLive -= Time.deltaTime;
        if (timeToLive <= 0) {
            Die();
        }
    }

    private void Die() {
        batch.particles.remove(this);
    }

}
