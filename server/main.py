import json
from typing import Dict
from fastapi import FastAPI, WebSocket, WebSocketDisconnect, HTTPException
from pydantic import BaseModel
from pstn_gateway import gateway

app = FastAPI(title='InternetCall Signaling Server', version='0.1.0')
connections: Dict[str, WebSocket] = {}

@app.get('/health')
async def health():
    return {'ok': True, 'service': 'internetcall'}

@app.websocket('/ws/{user_id}')
async def websocket_endpoint(websocket: WebSocket, user_id: str):
    await websocket.accept()
    connections[user_id] = websocket
    try:
        while True:
            message = json.loads(await websocket.receive_text())
            target = message.get('to')
            if target and target in connections:
                await connections[target].send_text(json.dumps({**message, 'from': user_id}))
            else:
                await websocket.send_text(json.dumps({'type':'error','message':'Target user is offline or missing.'}))
    except WebSocketDisconnect:
        if connections.get(user_id) is websocket:
            connections.pop(user_id, None)

class PSTNCall(BaseModel):
    to: str

@app.post('/api/pstn/call')
async def pstn_call(body: PSTNCall):
    try:
        return await gateway.place_call(body.to)
    except Exception as exc:
        raise HTTPException(status_code=502, detail=str(exc))
