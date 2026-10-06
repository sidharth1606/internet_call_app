package com.example.internetcall

/**
 * WebRTC media engine boundary.
 * The signaling server already relays offer/answer/ICE messages.
 * Production implementation should create PeerConnection, microphone AudioTrack,
 * SDP offer/answer, ICE candidate exchange, mute/speaker controls and TURN support.
 */
class CallEngine
