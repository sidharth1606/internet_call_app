# InternetCall — App-to-App + PSTN Starter

A starter project for:
- Android app-to-app Internet voice calling using WebRTC.
- Android app to ordinary mobile/landline through an authorized SIP/PSTN provider.

The PSTN provider integration is deliberately a server-side adapter. No telecom credentials are included.

## Architecture
Android A <-> FastAPI WebSocket signaling <-> Android B
Audio uses WebRTC, not the signaling server.

For telephone calls:
Android -> FastAPI -> authorized SIP/voice provider -> PSTN -> ordinary phone.

## Server
```bash
cd server
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn main:app --host 0.0.0.0 --port 8000
```
For a LAN test, set the Android signaling URL to `ws://YOUR_PC_IP:8000/ws/USER_ID`.
For production use HTTPS/WSS and authentication.

## Android
Open `android/` in Android Studio. Change `SIGNALING_HOST` in `MainActivity.kt`.
The included code establishes signaling and WebRTC components; SDP/ICE negotiation is isolated in `CallEngine.kt` as the next implementation step.

## PSTN
Set `PSTN_PROVIDER=mock` for safe testing. A real provider adapter must be implemented from the provider's official API/SIP documentation. Use only an authorized telecom route and comply with applicable regulations.

## Production checklist
Authentication, WSS, TURN, rate limits, fraud controls, verified caller ID, billing/minute limits, audit logs, privacy/consent, and telecom compliance.
