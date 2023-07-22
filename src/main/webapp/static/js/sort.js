/**
 * This JavaScript file is used for handling data fetching and
 * page navigation for a list of people.
 */

// Initialize default values for page number, items per page, and sort order.
let currentPage = 1;
const perPage = 10;
let sortOrder = 'id'; // Default sort order
let sortReverse = false; // Default sort direction

// Attach event handlers for form submission and button clicks
document.getElementById('sortForm').addEventListener('submit', sortFormSubmitHandler);
document.getElementById('prev-button').addEventListener('click', prevButtonHandler);
document.getElementById('next-button').addEventListener('click', nextButtonHandler);

/**
 * Event handler for sort form submission.
 * It updates the sortOrder based on user input and fetches the data accordingly.
 * If the user selects the same column, it toggles the sort direction.
 */
function sortFormSubmitHandler(event) {
  event.preventDefault();
  let newSortOrder = document.getElementById('sortOrder').value;

  if (sortOrder === newSortOrder) {
    sortReverse = !sortReverse; // Toggle sort direction
  } else {
    sortOrder = newSortOrder;
    sortReverse = false; // Reset sort direction to default
  }

  getPeople(currentPage, perPage, sortOrder, sortReverse);
}

/**
 * Event handler for previous page button click.
 * It decrements the currentPage (if not already at the first page) and fetches the data for that page.
 */
function prevButtonHandler() {
  if (currentPage > 1) {
    currentPage--;
    getPeople(currentPage, perPage, sortOrder, sortReverse);
  } else {
    alert("You're on the first page");
  }
}

/**
 * Event handler for next page button click.
 * It increments the currentPage and fetches the data for that page.
 */
function nextButtonHandler() {
  currentPage++;
  getPeople(currentPage, perPage, sortOrder, sortReverse);
}

/**
 * Fetches a page of people from the server.
 * The page number, items per page, sort order, and sort direction are configurable.
 */
function getPeople(page, size, sort, reverse) {
  fetch(`/people?page=${page}&size=${size}&sort=${sort}&reverse=${reverse}`)
    .then(handleFetchResponse)
    .then(handleFetchData)
    .catch(handleFetchError);
}

/**
 * Handles the response from the fetch request.
 * Throws an error if the response was not OK, otherwise returns the response data as JSON.
 */
function handleFetchResponse(response) {
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return response.json();
}

/**
 * Handles the JSON data from the fetch request.
 * If the data array is empty, it alerts the user that there is no more data.
 * Otherwise, it calls renderTable to update the table with the new data.
 */
function handleFetchData(data) {
  if (data.length === 0) {
    alert("No more data to fetch");
  } else {
    renderTable(data);
  }
}

/**
 * Handles any errors that occurred during the fetch request.
 */
function handleFetchError(error) {
  console.error('Error:', error);
}

/**
 * Renders a table of people based on an array of data.
 * Each person is represented as a row in the table, with cells for the logo ID, name, and surname.
 */
function renderTable(data) {
  let tableBody = document.querySelector('.user-list tbody');
  tableBody.innerHTML = ''; // Clear the current table body

  // Construct new table rows based on data
  data.forEach(person => {
    tableBody.innerHTML += `
      <tr>
        <td><img src="/static/logo/${person.logoId}.png" alt="User Logo" width="45" height="45" class="round-image"></td>
        <td>${person.name}</td>
        <td>${person.surname}</td>
      </tr>
    `;
  });
}

// Call getPeople once at the start to populate the initial data
getPeople(currentPage, perPage, sortOrder, sortReverse);
