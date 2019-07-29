import { ACCESS_TOKEN, API_BASE_URL, POLL_LIST_SIZE } from '../constants';

const request = (options) => {
  const headers = new Headers({
    'Content-Type': 'application/json',
  });

  if (localStorage.getItem(ACCESS_TOKEN)) {
    headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
  }

  const defaults = { headers: headers };
  options = Object.assign({}, defaults, options);

  return fetch(options.url, options)
    .then(response =>
      response.json().then(json => {
        if (!response.ok) {
          return Promise.reject(json);
        }
        return json;
      })
    );
};

export const getAllPolls = (page, size) => {
  page = page || 0;
  size = size || POLL_LIST_SIZE;

  return request({
    url: `${API_BASE_URL}/polls?page=${page}&size=${size}`,
    method: 'GET'
  });
};

export const createPoll = (pollData) => {
  return request({
    url: `${API_BASE_URL}/polls`,
    method: 'POST',
    body: JSON.stringify(pollData)
  });
};

export const castVote = (voteData) => {
  return request({
    url: `${API_BASE_URL}/polls/${voteData.pollId}/votes`,
    method: 'POST',
    body: JSON.stringify(voteData)
  });
};

export const login = (loginRequest) => {
  return request({
    url: `${API_BASE_URL}/auth/signin`,
    method: 'POST',
    body: JSON.stringify(loginRequest)
  });
};

export const signup = (signupRequest) => {
  return request({
    url: `${API_BASE_URL}/auth/signup`,
    method: 'POST',
    body: JSON.stringify(signupRequest)
  });
};

export const checkUsernameAvailability = (username) => {
  return request({
    url: `${API_BASE_URL}/user/checkUsernameAvailability?username=${username}`,
    method: 'GET'
  });
};

export const checkEmailAvailability = (email) => {
  return request({
    url: `${API_BASE_URL}/user/checkEmailAvailability?email=${email}`,
    method: 'GET'
  });
};


export const getCurrentUser = () => {
  if (!localStorage.getItem(ACCESS_TOKEN)) {
    return Promise.reject('No access token set.');
  }

  return request({
    url: `${API_BASE_URL}/user/me`,
    method: 'GET'
  });
};

export const getUserProfile = (username) => {
  return request({
    url: `${API_BASE_URL}/users/${username}`,
    method: 'GET'
  });
};

export const getUserCreatedPolls = (username, page, size) => {
  page = page || 0;
  size = size || POLL_LIST_SIZE;

  return request({
    url: `${API_BASE_URL}/users/${username}/polls?page=${page}&size=${size}`,
    method: 'GET'
  });
};

export const getUserVotedPolls = (username, page, size) => {
  page = page || 0;
  size = size || POLL_LIST_SIZE;

  return request({
    url: `${API_BASE_URL}/users/${username}/votes?page=${page}&size=${size}`,
    method: 'GET'
  });
};
