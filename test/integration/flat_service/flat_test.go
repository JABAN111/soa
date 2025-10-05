package flat_service

import (
	"bytes"
	"encoding/json"
	"io"
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

const wildflyAddr = "http://localhost:23210/flat_service"

const (
	healthPing = wildflyAddr + "/health"
	getAllFlat = wildflyAddr + "/api/flats"
	createFlat = wildflyAddr + "/api/flats"
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

	body, err := io.ReadAll(resp.Body)
	require.NoError(t, err)
	var flats []Flat
	require.NoError(t, json.Unmarshal(body, &flats))
	t.Logf("flats: %+v", flats)
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
			expectCode:  http.StatusCreated,
		},
		{
			testName: "Missing coordinates",
			flat: Flat{
				Name:              "simple flat",
				Area:              252,
				NumberOfRooms:     2,
				NumberOfBathrooms: 321,
				Furnish:           "LITTLE",
				Transport:         "FEW",
			},
			expectError: false,
			expectCode:  http.StatusCreated,
		},
	}
	for _, tc := range testCases {
		t.Run(tc.testName, func(t *testing.T) {
			httpCl := util.GetHttpClient()

			body, err := json.Marshal(tc.flat)
			require.NoError(t, err)

			req, err := http.NewRequest("POST", createFlat, bytes.NewReader(body))
			require.NoError(t, err)
			req.Header.Add("Content-Type", "application/json")
			resp, err := httpCl.Do(req)
			if tc.expectError {
				data, err := io.ReadAll(resp.Body)
				require.NoError(t, err)
				t.Logf("response: %s", string(data))
				require.Truef(t, resp.StatusCode >= 400 || resp.StatusCode < 500, "Response status code: %v", resp.StatusCode)
				return
			}
			require.NoError(t, err)

			var message bytes.Buffer
			_, err = message.ReadFrom(resp.Body)
			require.NoError(t, err)
			require.Equal(t, tc.expectCode, resp.StatusCode, message.String())
		})
	}
}
