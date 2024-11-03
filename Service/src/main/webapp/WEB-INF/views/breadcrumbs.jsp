<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="breadcrumb-container">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb" id="breadcrumb">
            <!-- Breadcrumb items will be dynamically inserted here -->
        </ol>
    </nav>
</div>

<script>
    // Initialize an array to store breadcrumb history
    var breadcrumbHistory = JSON.parse(localStorage.getItem('breadcrumbHistory')) || [];

    // Function to update the breadcrumb display
    function updateBreadcrumbs() {
        var breadcrumbList = document.getElementById('breadcrumb');
        breadcrumbList.innerHTML = ''; // Clear existing breadcrumbs

        // Add Home link
        var homeItem = document.createElement('li');
        homeItem.className = 'breadcrumb-item';
        homeItem.innerHTML = '<a href="http://localhost:8081/admin/home">Home</a>';
        breadcrumbList.appendChild(homeItem);

        // Add other items from history
        breadcrumbHistory.forEach(function(item, index) {
            var breadcrumbItem = document.createElement('li');
            breadcrumbItem.className = 'breadcrumb-item';

            if (index === breadcrumbHistory.length - 1) {
                // Current page, do not link
                breadcrumbItem.textContent = item.name;
            } else {
                // Create link for previous pages with a click event
                breadcrumbItem.innerHTML = '<a href="#" onclick="navigateToPage(\'' + item.url + '\', ' + index + ')">' + item.name + '</a>';
            }
            breadcrumbList.appendChild(breadcrumbItem);
        });
    }

    // Function to add a new page to the breadcrumb history
    function addBreadcrumb(name, url) {
        // Check if the current URL or breadcrumb name already exists in history
        var existingIndex = breadcrumbHistory.findIndex(function(item) {
            return item.name === name && item.url === url;
        });

        if (existingIndex !== -1) {
            // If the current page exists in the breadcrumb history, truncate history after this item
            breadcrumbHistory = breadcrumbHistory.slice(0, existingIndex + 1);
        } else {
            // Otherwise, add the new breadcrumb if it's not a duplicate
            breadcrumbHistory.push({ name: name, url: url });
        }

        // Save updated breadcrumb history in localStorage
        localStorage.setItem('breadcrumbHistory', JSON.stringify(breadcrumbHistory));

        // Update the breadcrumb display
        updateBreadcrumbs();
    }

    // Function to navigate to a page and update breadcrumbs
    function navigateToPage(url, index) {
        // Remove items from the breadcrumb history after the clicked index
        breadcrumbHistory = breadcrumbHistory.slice(0, index + 1);
        localStorage.setItem('breadcrumbHistory', JSON.stringify(breadcrumbHistory));
        window.location.href = url; // Navigate to the selected page
    }

    // Call this function on page load to add the current page
    $(document).ready(function () {
        var currentPageName = document.title; // Get the page title as the name
        var currentPageUrl = window.location.href; // Get the current URL
        addBreadcrumb(currentPageName, currentPageUrl);

        // Handle logout
        $('#logoutButton').on('click', function() {
            localStorage.removeItem('breadcrumbHistory'); // Clear history on logout
        });
    });
</script>


<style>
    .breadcrumb-container {
        margin: 20px 0;
    }
    .breadcrumb {
        background-color: white;
        border-radius: 0.25rem;
    }
    .breadcrumb-item + .breadcrumb-item::before {
        content: ">";
        padding: 0 5px;
        color: #6c757d;
    }
    .breadcrumb-item a {
        text-decoration: none;
        color: #007bff;
    }
    .breadcrumb-item a:hover {
        text-decoration: underline;
    }
</style>