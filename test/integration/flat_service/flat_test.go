package flat_service

import (
	"net/http"
	"soa/util"
	"testing"

	"github.com/stretchr/testify/require"
)

type Flat struct {
	Name        string `json:"name"`
	Coordinates struct {
		X int `json:"x"`
		Y int `json:"y"`
	} `json:"coordinates"`
	Area              int    `json:"area"`
	NumberOfRooms     int    `json:"numberOfRooms"`
	NumberOfBathrooms int    `json:"numberOfBathrooms"`
	Furnish           string `json:"furnish"`
	Transport         string `json:"transport"`
}

const wildflyAddr = "http://localhost:8080/flat_service"

const (
	healthPing = wildflyAddr + "/health"
	getAllFlat = wildflyAddr + "/api/flats"
)

func TestHealthPing(t *testing.T) {
	httpCl := util.GetHttpClient()

	req, err := http.NewRequest("GET", healthPing, nil)
	require.NoError(t, err)
	resp, err := httpCl.Do(req)
	require.NoError(t, err)
	require.Equal(t, http.StatusOK, resp.StatusCode)
}

func TestGetAllFlat(t *testing.T) {
	httpCl := util.GetHttpClient()

	req, err := http.NewRequest("GET", getAllFlat, nil)
	require.NoError(t, err)
	resp, err := httpCl.Do(req)
	require.NoError(t, err)
	require.Equal(t, http.StatusOK, resp.StatusCode)
}

func TestCreateFlat(t *testing.T) {
	testCases := []struct {
		testName    string
		flat        Flat
		expectError bool
		expectCode  int
	}{
		{
			testName: "Simple creating",
			flat: Flat{
				Name: "simple flat",
				Coordinates: struct {
					X int `json:"x"`
					Y int `json:"y"`
				}{
					X: 1,
					Y: 2,
				},
				Area:              252,
				NumberOfRooms:     2,
				NumberOfBathrooms: 321,
				Furnish:           "LITTLE",
				Transport:         "FEW",
			},
			expectError: false,
			expectCode:  0,
		},
	}
	for _, tc := range testCases {
		t.Run(tc.testName, func(t *testing.T) {
			httpCl := util.GetHttpClient()
			req, err := http.NewRequest("POST", wildflyAddr+"/api/flats", nil)
			require.NoError(t, err)
			resp, err := httpCl.Do(req)
			if tc.expectError {
				require.Truef(t, resp.StatusCode >= 400 || resp.StatusCode < 500, "Response status code: %v", resp.StatusCode)
				return
			}
			require.NoError(t, err)
			require.Equal(t, tc.expectCode, resp.StatusCode)
		})
	}
}
