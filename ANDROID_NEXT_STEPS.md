# Android next steps

The package is a safe starter, not a claimed production-ready telephony app.

1. Set `SIGNALING_HOST` to your PC/server LAN or HTTPS/WSS endpoint.
2. Complete `CallEngine.kt` with PeerConnectionObserver, SDP offer/answer and ICE.
3. Add STUN/TURN configuration; production mobile networks generally need TURN fallback.
4. Add incoming-call UI, mute, speaker/Bluetooth, timer and call state.
5. Add authentication and WSS before Internet deployment.
6. For PSTN, implement a provider adapter from the official documentation of your authorized provider. Keep secrets only on the server.
