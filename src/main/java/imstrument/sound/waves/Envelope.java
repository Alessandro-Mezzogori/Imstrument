package imstrument.sound.waves;

public class Envelope {
    EnvelopeState state;
    /*
     *  attack params
     *  attack equation: amplitude = maxAmplitude*(e^(attackVelocity*time) - 1)/(e^(attackVelocity*attackTime) - 1)
     * */
    /**
     * time in seconds to reach max amplitude
     */
    private double attackTime;
    /**
     * coefficient for the attack curve the larger it is the faster the sound reaches max amplitude
     */
    private double attackVelocity;
    /* decay params*/
    private double decayTime;
    /* sustain params*/

    /* release params */
    /**
     * time in seconds to reach an amplitude of 0.0
     */
    private double releaseTime;
    private double releaseVelocity;
    private boolean release;
    private double startReleaseTime;
    private double startReleaseAmplifier;

    /* calculations optimization attributes */
    private double attackDenominator;
    private double releaseDenominator;

    /* saving amplifier */
    public Envelope(double attackTime, double attackVelocity, double releaseTime, double releaseVelocity){
        this.attackTime = attackTime;
        this.attackVelocity = attackVelocity;
        computeAttackDenominator();

        this.decayTime = 0.0;

        //sustain

        this.releaseTime = releaseTime;
        this.releaseVelocity = releaseVelocity;
        computeReleaseDenominator();
    }

    public double getAmplitudeAmplifier(double time){
        double amplifier = switch (this.state){
            case ATTACK -> (Math.exp(attackVelocity*time)-1.0)/attackDenominator;
            case DECAY ->  1.0;
            case SUSTAIN -> 1.0;
            case RELEASE -> startReleaseAmplifier*(1.0 - (Math.exp(releaseVelocity*(time - startReleaseTime))-1.0)/releaseDenominator);
            case RELEASED -> 0.0;
        };
        updateEnvelopeState(time, amplifier);


        return amplifier;
    }

    private void updateEnvelopeState(double time, double amplifier){
        // decay, sustain, release
        if(state == EnvelopeState.RELEASED)
            return;

        if(state == EnvelopeState.RELEASE && amplifier <= 0.0) {
            state = EnvelopeState.RELEASED;
            return;
        }


        if(this.release){
            state = EnvelopeState.RELEASE;
            return;
        }

        startReleaseTime = time;
        startReleaseAmplifier = amplifier;
        if (time < attackTime) {
            state = EnvelopeState.ATTACK;
            return;
        }

        if(false){
            state = EnvelopeState.DECAY;
            return;
        }
        state = EnvelopeState.SUSTAIN;
    }

    public void startRelease(){
        release = true;
    }

    public void reset(){
        state = EnvelopeState.ATTACK;
        release = false;
    }

    /* utils */
    private void computeAttackDenominator(){
        attackDenominator = Math.exp(attackVelocity* attackTime) - 1.0;
    }

    private void computeReleaseDenominator(){
        releaseDenominator = Math.exp(releaseVelocity* releaseTime) - 1.0;
    }

    /* setters */
    public void setAttack(double attackTime, double attackVelocity){
        this.attackTime = attackTime;
        this.attackVelocity = attackVelocity;
    }
}
