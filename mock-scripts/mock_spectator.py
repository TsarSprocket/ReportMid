import json
from mitmproxy import http
from pathlib import Path

_MOCK_DIR = Path(__file__).parent.parent / "mocks" / "spectator-v5"
_RESPONSE_FILE = _MOCK_DIR / "ongoing-response.json"
_HEADERS_FILE = _MOCK_DIR / "ongoing-headers.json"


class SpectatorMock:
    def request(self, flow: http.HTTPFlow) -> None:
        if "/lol/spectator/v5/active-games/by-summoner/" in flow.request.pretty_url:
            headers = json.loads(_HEADERS_FILE.read_text(encoding="utf-8"))
            flow.response = http.Response.make(
                200,
                _RESPONSE_FILE.read_bytes(),
                headers,
            )


addons = [SpectatorMock()]
