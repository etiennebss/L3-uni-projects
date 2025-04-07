import API_URL from "./apiUrl.js"

import fetch from "node-fetch"

const CREATE_TODO = `
mutation CreateTodos($input: [TodoCreateInput!]!) {
  createTodos(input: $input) {
    todos {
      id
      content
      done
    }
  }
}
`

export function createTodo(content, todoListId, token) {
  return fetch(API_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'authorization': 'Bearer ' + token
    },
    body: JSON.stringify({
      query: CREATE_TODO,
      variables: {
        "input": [
          {
            "belongsTo": {
              "connect": {
                "where": {
                  "id": todoListId
                }
              }
            },
            "content": content
          }
        ]
      }
    })
  })
    .then(response => {
      return response.json()
    })
    .then(jsonResponse => {
      if (jsonResponse.errors != null) {
        throw jsonResponse.errors[0]
      }
      return jsonResponse.data.createTodos.todos[0]
    })
    .catch(error => {
        console.log('error API', error.message)
      throw error
    })
}

const TODOS = `
query Todos($where: TodoWhere) {
  todos(where: $where) {
    id
    content
    done
  }
}
`
export function getTodos(todoListId, token) {
  return fetch(API_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'authorization': 'Bearer ' + token
    },
    body: JSON.stringify({
      query: TODOS,
      variables: {
        "where": {
          "belongsTo": {
            "id": todoListId
          }
        }
      }
    })
  })
    .then(response => {
      return response.json()
    })
    .then(jsonResponse => {
      if (jsonResponse.errors != null) {
        throw jsonResponse.errors[0]
      }
      return jsonResponse.data.todos
    })
    .catch(error => {
        console.log('error API', error.message)
      throw error
    })
}

const UPDATE_TODO = `
mutation UpdateTodos($where: TodoWhere, $update: TodoUpdateInput) {
  updateTodos(where: $where, update: $update) {
    todos {
      id
      content
      done
    }
  }
}`
export function updateTodo(todoId, done, token) {
    return fetch(API_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'authorization': 'Bearer ' + token
      },
      body: JSON.stringify({
        query: UPDATE_TODO,
        variables: {
          "where": {
            "id": todoId
          },
          "update": {
            "done": done
          }
        }
      })
    })
      .then(response => {
        return response.json()
      })
      .then(jsonResponse => {
        if (jsonResponse.errors != null) {
          throw jsonResponse.errors[0]
        }
        return jsonResponse.data.updateTodos.todos[0]
      })
      .catch(error => {
          console.log('error API', error.message)
        throw error
      })
  }
  
const DELETE_TODO = `
mutation($id: ID!) {
  deleteTodos(where: { id: $id }) {
    nodesDeleted
  }
}
`

export function deleteTodo(id, token) {
  return fetch(API_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'authorization': 'Bearer ' + token
    },
    body: JSON.stringify({
      query: DELETE_TODO,
      variables: {
        id: id
      }
    })
  })
    .then(response => {
      return response.json()
    })
    .then(jsonResponse => {
      if (jsonResponse.errors != null) {
        throw jsonResponse.errors[0]
      }
      return jsonResponse.data.deleteTodos.nodesDeleted
    })
    .catch(error => {
        console.log('error API', error.message)
      throw error
    })
}
