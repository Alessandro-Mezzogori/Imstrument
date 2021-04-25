package imstrument.sound.waves;

/**
 * Object modeling the ADSR (Attack, Decay, Sustain, Release) Envelope of the amplitude of a sound wave
 */
public class Envelope {
    EnvelopeState state;
    /*
     *  attack params
     *  attack equation: amplitude = maxAmplitude*(e^(attackVelocity*time) - 1)/(e^(attackVelocity*attackTime) - 1)
     * */
    /**
     * time in seconds to reach amplifier peak
     */
    private double attackTime;
    /**
     * coefficient for the attack curve the larger it is the faster the sound reaches amplifier peak
     * must be != 0
     */
    private double attackVelocity;
    /**
     * max amplifier reached at the end of the attack curve
     */
    private double attackAmplifierPeak;

    /* decay params*/
    /**
     * time in seconds to reach the sustain amplifier value
     */
    private double decayTime;
    /**
     * coefficient for the decay curve, the larger it is the faster the sound reaches the sustain amplifier value
     */
    private double decayVelocity;

    private double startDecayTime;
    /* sustain params*/
    /**
     *
     */
    private double sustainAmplifier;

    /* release params */
    /**
     * time in seconds to reach an amplitude of 0.0
     */
    private double releaseTime;
    /**
     * coefficient for the realease curve, the larger it is the faster the sound reaches 0.0 amplitude
     * must be != 0
     */
    private double releaseVelocity;
    /**
     * state attribute, indicates if the envelope should enter the release phase
     */
    private boolean release;
    /**
     * saves the starting time of the release to translate the function on the x axis
     */
    private double startReleaseTime;
    /**
     * stores the start amplifier valuer to have a smooth release in any part of the envolope
     */
    private double startReleaseAmplifier;
    /**
     * lower threshold of the release curve
     */
    private final double releaseAmplifierThreshold = 0.01;

    /* calculations optimization attributes */
    private double attackDenominator;
    private double releaseDenominator;
    private double decayDenominator;
    /* saving amplifier */

    /**
     *
     * @param attackTime time in seconds to reach attackAmplifierPeak
     * @param attackVelocity coefficient for the attack curve the larger it is the faster the sound reaches amplifier peak
     * @param attackAmplifierPeak maximum amplifier value reached at the end of the attack curve value between [0.0, 1.0] (outside of it will be corrected to 0.0 or 1.0)
     * @param decayTime time in seconds to reach the sustain amplifier value
     * @param decayVelocity coefficient for the decay curve, the larger it is the faster the sound reaches the sustain amplifier value
     * @param sustainAmplifier value maintained during the sustain curve  value between [0.0, 1.0] (outside of it will be corrected to 0.0 or 1.0)
     * @param releaseTime time in seconds to reach an amplitude of 0.0
     * @param releaseVelocity coefficient for the realease curve, the larger it is the faster the sound reaches 0.0 amplitude
     */
    public Envelope(double attackTime, double attackVelocity, double attackAmplifierPeak, double decayTime, double decayVelocity, double sustainAmplifier, double releaseTime, double releaseVelocity){
        /*
            TODO add checks for:
                - attackAmplifierPeak [0.0, 1.0]
                - sustainAmplifier [0.0, 1.0]
         */
        this.attackTime = attackTime;
        this.attackVelocity = attackVelocity;

        this.attackAmplifierPeak = attackAmplifierPeak;
        computeAttackDenominator();

        this.decayTime = decayTime;
        this.decayVelocity = decayVelocity;
        computeDecayDenominator();

        this.sustainAmplifier = sustainAmplifier;

        this.releaseTime = releaseTime;
        this.releaseVelocity = releaseVelocity;
        computeReleaseDenominator();
    }

    public Envelope(){
        this(0.0, 1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0);
    }

    /**
     * returns the coefficient at the time of the wave generation
     * @param time time in seconds from the start of the soundwave generation
     * @return number between [0.0, attackAmplifierPeak] with a max of [0.0, 1.0]
     */
    public double getAmplitudeAmplifier(double time){
        double amplifier = switch (this.state){
            case ATTACK -> attackAmplifierPeak*(attackTime <= 0.0 ? 1.0 : (Math.exp(attackVelocity*time)-1.0)/attackDenominator);
            case DECAY ->  attackAmplifierPeak - (attackAmplifierPeak - sustainAmplifier)*(decayTime <= 0 ? 1.0 : (Math.exp(decayVelocity*(time - startDecayTime))-1.0)/decayDenominator);
            case SUSTAIN -> sustainAmplifier;
            case RELEASE -> startReleaseAmplifier*(releaseTime <= 0.0 ? 0.0 : (1.0 - (Math.exp(releaseVelocity*(time - startReleaseTime))-1.0)/releaseDenominator));
            case RELEASED -> 0.0;
        };
        updateEnvelopeState(time, amplifier);

        return amplifier; //normalizes to values beetwen [0,1]
    }

    /**
     * updates the envelope state by the time and amplifier
     * @param time time in seconds from the start of the soundwave generation
     * @param amplifier number between [0.0, attackAmplifierPeak]
     */
    private void updateEnvelopeState(double time, double amplifier){
        // decay, sustain, release
        if(state == EnvelopeState.RELEASED)
            return;

        /* stops the release if under 1% of the maxAmplitudea
        *  if the gap is audible lower the
        * */
        if(state == EnvelopeState.RELEASE && amplifier <= releaseAmplifierThreshold) {
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
            startDecayTime = time;
            return;
        }

        if(time < attackTime + decayTime){
            state = EnvelopeState.DECAY;
            return;
        }
        state = EnvelopeState.SUSTAIN;
    }

    /**
     * starts the envelope release
     */
    public void startRelease(){
        release = true;
    }

    /**
     * resets the envelope to the initial state
     */
    public void reset(){
        state = EnvelopeState.ATTACK;
        release = false;
    }

    /**
     * imports the settings of another envolope object for performance reasons
     * @param envelope settings source
     */
    public void importSettings(Envelope envelope){
        this.attackTime = envelope.attackTime;
        this.attackVelocity = envelope.attackVelocity;
        this.attackAmplifierPeak = envelope.attackAmplifierPeak;

        this.decayTime = envelope.decayTime;
        this.decayVelocity = envelope.decayVelocity;

        this.sustainAmplifier = envelope.sustainAmplifier;

        this.releaseTime = envelope.releaseTime;
        this.releaseVelocity = envelope.releaseVelocity;

        computeReleaseDenominator();
        computeDecayDenominator();
        computeAttackDenominator();
    }

    /* computer*Denominator, optimization to speed up the generation of the AmplitudeAmplifier*/
    private void computeAttackDenominator(){
        attackDenominator = Math.exp(attackVelocity* attackTime) - 1.0;
    }

    private void computeReleaseDenominator(){
        releaseDenominator = Math.exp(releaseVelocity* releaseTime) - 1.0;
    }

    private void computeDecayDenominator(){decayDenominator = Math.exp(decayVelocity* decayTime) - 1.0;}

    @Override
    public String toString() {
        return "Attack: " + attackTime + " " + attackVelocity +
                "\nDecay " + decayTime + " " + decayVelocity +
                "\nSustain: " + sustainAmplifier +
                "\nRelease: " + releaseTime + " " + releaseVelocity + "\n";
    }
}
