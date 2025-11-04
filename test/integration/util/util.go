package util

import (
	"crypto/tls"
	"net/http"
)

func GetHttpClient() *http.Client {
	return &http.Client{
		Transport: &http.Transport{TLSClientConfig: &tls.Config{InsecureSkipVerify: true}},
	}
}
