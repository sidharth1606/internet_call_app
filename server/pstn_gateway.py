import os
from abc import ABC, abstractmethod
from dotenv import load_dotenv
load_dotenv()

class PSTNGateway(ABC):
    @abstractmethod
    async def place_call(self, destination: str) -> dict: ...

class MockPSTNGateway(PSTNGateway):
    async def place_call(self, destination: str) -> dict:
        return {'ok': False, 'mode':'mock', 'destination':destination,
                'message':'PSTN provider is not configured. No real telephone call was placed.'}

class ProviderGateway(PSTNGateway):
    async def place_call(self, destination: str) -> dict:
        raise RuntimeError('Implement this adapter using the official API/SIP documentation of your authorized provider.')

gateway = ProviderGateway() if os.getenv('PSTN_PROVIDER','mock').lower() == 'provider' else MockPSTNGateway()
