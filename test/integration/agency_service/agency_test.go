package agency_service

import (
	"bytes"
	"encoding/json"
	"io"
	"net/http"
	"net/url"
	"soa/util"
	"testing"

	"github.com/stretchr/testify/require"
)

const payaraAddr = "https://localhost:23223/agency"

const (
	getTotalCost   = payaraAddr + "/get-total-cost"
	getBiggestCost = payaraAddr + "/get-most-expensive"
	getHealthPoint = payaraAddr + "/health"
)

func TestHealth(t *testing.T) {
	httpCl := util.GetHttpClient()

	req, err := http.NewRequest("GET", getHealthPoint, nil)
	require.NoError(t, err)

	resp, err := httpCl.Do(req)
	require.NoError(t, err)
	require.Equal(t, http.StatusOK, resp.StatusCode)
}

func TestPush(t *testing.T) {
	httpCl := util.GetHttpClient()

	type PushData struct {
		FlatID        int64 `json:"flat_id"`
		NumberOfRooms int64 `json:"number_of_rooms"`
	}

	p := PushData{
		FlatID:        1234,
		NumberOfRooms: 1,
	}
	jsonData, err := json.Marshal(p)
	require.NoError(t, err)
	jsonReader := bytes.NewReader(jsonData)
	req, err := http.NewRequest("POST", payaraAddr+"/push", jsonReader)
	require.NoError(t, err)

	req.Header.Add("Content-Type", "application/json")
	resp, err := httpCl.Do(req)
	require.NoError(t, err)

	responseB, err := io.ReadAll(resp.Body)
	t.Logf("response: %v", resp.StatusCode)
	t.Logf("body: %v", string(responseB))
}

func TestGetTotalCost(t *testing.T) {
	httpCl := util.GetHttpClient()
	req, err := http.NewRequest("GET", getTotalCost, nil)
	require.NoError(t, err)
	resp, err := httpCl.Do(req)
	require.NoError(t, err)
	require.Equal(t, http.StatusOK, resp.StatusCode)

	body, err := io.ReadAll(resp.Body)
	require.NoError(t, err)
	t.Log(string(body))
}

func TestGetBiggestCost(t *testing.T) {
	testCases := []struct {
		testName    string
		first       string
		second      string
		third       string
		expectedErr bool
	}{
		{
			testName:    "Normal test",
			first:       "1",
			second:      "2",
			third:       "3",
			expectedErr: false,
		},
		{
			testName:    "Normal test reverse",
			first:       "3",
			second:      "2",
			third:       "1",
			expectedErr: false,
		},
		{
			testName:    "Same floats",
			first:       "1",
			second:      "1",
			third:       "1",
			expectedErr: false,
		},
		{
			testName:    "Unexistence floats",
			first:       "0",
			second:      "-999",
			third:       "8988881882",
			expectedErr: true,
		},
		{
			testName:    "no num",
			first:       "bread",
			second:      "home",
			third:       "3",
			expectedErr: true,
		},
	}

	for _, tc := range testCases {
		t.Run(tc.testName, func(t *testing.T) {
			httpCl := util.GetHttpClient()
			getBiggestCostPath, err := url.JoinPath(getBiggestCost, tc.first, tc.second, tc.third)
			require.NoError(t, err)

			req, err := http.NewRequest("GET", getBiggestCostPath, nil)
			require.NoError(t, err)

			resp, err := httpCl.Do(req)
			if tc.expectedErr {
				require.Truef(t, resp.StatusCode >= 400 || resp.StatusCode < 500, "Response status code: %v", resp.StatusCode)
				return
			}
			require.NoError(t, err)
		})
	}
}
