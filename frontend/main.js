// =====================================================
// ASCENDING CLOUD STAFFING - MAIN JAVASCRIPT
// =====================================================

// -----------------------------
// JOB DATA
// -----------------------------

const jobs = [
    {
        id: 1,
        title: "Senior Cloud Engineer",
        company: "Ascending Cloud Client",
        location: "Remote - India",
        type: "Full-time",
        salary: "$3000",
        platform: "AWS",
        experience: "5+ Years",
        workplace: "Remote",
        tags: ["AWS", "Terraform", "Docker"]
    },
    {
        id: 2,
        title: "Azure DevOps Engineer",
        company: "Technology Partner",
        location: "Bengaluru, India",
        type: "Full-time",
        salary: "$4000",
        platform: "Azure",
        experience: "3+ Years",
        workplace: "Hybrid",
        tags: ["Azure", "DevOps", "CI/CD"]
    },
    {
        id: 3,
        title: "GCP Data Engineer",
        company: "Technology Partner",
        location: "Hyderabad, India",
        type: "Contract",
        salary: "$2500",
        platform: "GCP",
        experience: "3+ Years",
        workplace: "On-site",
        tags: ["GCP", "Python", "SQL"]
    },
    {
        id: 4,
        title: "Full Stack Developer",
        company: "Ascending Cloud Client",
        location: "Noida, India",
        type: "Full-time",
        salary: "$3500",
        platform: "AWS",
        experience: "2+ Years",
        workplace: "Hybrid",
        tags: ["React", "Node.js", "AWS"]
    },
    {
        id: 5,
        title: "Cyber Security Analyst",
        company: "Technology Partner",
        location: "Pune, India",
        type: "Full-time",
        salary: "$3000",
        platform: "Azure",
        experience: "2+ Years",
        workplace: "Remote",
        tags: ["SIEM", "SOC", "Security"]
    }
];

let activeFilters = {
    keyword: "",
    location: "",
    platform: "all",
    jobType: "all",
    workplaces: [],
    experiences: []
};

let currentJobList = [...jobs];


// -----------------------------
// UTILITIES
// -----------------------------

function escapeHtml(value) {
    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#39;");
}

function normalizePath(path) {
    return path.replace(/\\/g, "/");
}

function isSubfolderPage() {
    const path = normalizePath(window.location.pathname);
    return /\/(candidate|employer)\//.test(path);
}

function isJobsPage() {
    const path = normalizePath(window.location.pathname);
    return /\/jobs\.html$/.test(path);
}

function pageLink(relativePath) {
    const normalized = relativePath.replace(/\\/g, "/");

    if (normalized === "../jobs.html" || normalized.startsWith("../jobs.html?")) {
        const query = normalized.includes("?") ? normalized.slice(normalized.indexOf("?")) : "";
        if (isSubfolderPage()) {
            return `../jobs.html${query}`;
        }
        return `jobs.html${query}`;
    }

    if (isSubfolderPage()) {
        if (normalized.startsWith("../")) {
            return normalized;
        }
        return `../${normalized}`;
    }

    return normalized;
}

function getDashboardPath(role) {
    const dashboards = {
        candidate: "candidate/dashboard.html",
        employer: "employer/post-job.html",
        recruiter: "index.html",
        admin: "index.html"
    };

    return dashboards[role] || "index.html";
}


// =====================================================
// NAVBAR
// =====================================================

function navbar() {
    const loggedIn = isLoggedIn();
    const role = localStorage.getItem("userRole");

    const authActions = loggedIn
        ? `
            <a href="${pageLink(getDashboardPath(role))}" class="btn light">
                Dashboard
            </a>
            <button type="button" class="btn outline" onclick="logout()">
                Logout
            </button>
        `
        : `
            <a href="${pageLink("login.html")}" class="btn light">
                Candidate Login
            </a>
            <a href="${pageLink("login.html?role=employer")}" class="btn">
                Employer Login
            </a>
        `;

    return `
        <header class="header">
            <div class="container nav">
                <a href="${pageLink("index.html")}" class="logo">
                    Ascending <span>Cloud</span>
                </a>

                <button
                    type="button"
                    class="mobile-menu-btn"
                    aria-label="Toggle navigation menu"
                    aria-expanded="false"
                    onclick="toggleMenu(this)"
                >
                    <span></span>
                    <span></span>
                    <span></span>
                </button>

                <nav class="navlinks" id="primaryNav">
                    <a href="${pageLink("index.html")}">Home</a>
                    <a href="${pageLink("jobs.html")}">Find Jobs</a>
                    <a href="${pageLink("hire-talent.html")}">Hire Talent</a>
                    <a href="${pageLink("services.html")}">Services</a>
                    <a href="${pageLink("about.html")}">About Us</a>
                    <a href="${pageLink("contact.html")}">Contact</a>
                </nav>

                <div class="actions">
                    ${authActions}
                </div>
            </div>
        </header>
    `;
}


function loadNavbar() {
    const navElement = document.getElementById("siteNav");
    if (navElement) {
        navElement.innerHTML = navbar();
    }
}


// =====================================================
// FOOTER
// =====================================================

function footer() {
    return `
        <footer class="footer">
            <div class="container">
                <div class="footer-grid">
                    <div>
                        <h3>Ascending Cloud</h3>
                        <p>
                            Connecting talented professionals
                            with the right opportunities.
                        </p>
                    </div>

                    <div>
                        <h4>For Candidates</h4>
                        <a href="${pageLink("jobs.html")}">Find Jobs</a>
                        <a href="${pageLink("signup.html")}">Create Account</a>
                        <a href="${pageLink("candidate/dashboard.html")}">Candidate Dashboard</a>
                    </div>

                    <div>
                        <h4>For Employers</h4>
                        <a href="${pageLink("hire-talent.html")}">Hire Talent</a>
                        <a href="${pageLink("login.html?role=employer")}">Employer Login</a>
                        <a href="${pageLink("employer/post-job.html")}">Post a Job</a>
                    </div>

                    <div>
                        <h4>Company</h4>
                        <a href="${pageLink("about.html")}">About Us</a>
                        <a href="${pageLink("services.html")}">Services</a>
                        <a href="${pageLink("contact.html")}">Contact</a>
                    </div>
                </div>

                <div class="footer-bottom">
                    <p>© 2026 Ascending Cloud Staffing. All rights reserved.</p>
                </div>
            </div>
        </footer>
    `;
}

function loadFooter() {
    const footerElement = document.getElementById("siteFooter");
    if (footerElement) {
        footerElement.innerHTML = footer();
    }
}


// =====================================================
// JOB FILTERING
// =====================================================

function applyFilters() {
    const filteredJobs = jobs.filter(job => {
        const keyword = activeFilters.keyword;
        const location = activeFilters.location;

        const matchesKeyword =
            !keyword ||
            job.title.toLowerCase().includes(keyword) ||
            job.company.toLowerCase().includes(keyword) ||
            job.platform.toLowerCase().includes(keyword) ||
            job.tags.some(tag => tag.toLowerCase().includes(keyword));

        const matchesLocation =
            !location ||
            job.location.toLowerCase().includes(location);

        const matchesPlatform =
            activeFilters.platform === "all" ||
            job.platform.toLowerCase() === activeFilters.platform.toLowerCase();

        const matchesType =
            activeFilters.jobType === "all" ||
            job.type.toLowerCase() === activeFilters.jobType.toLowerCase();

        const matchesWorkplace =
            activeFilters.workplaces.length === 0 ||
            activeFilters.workplaces.includes(job.workplace);

        const matchesExperience =
            activeFilters.experiences.length === 0 ||
            activeFilters.experiences.includes(job.experience);

        return (
            matchesKeyword &&
            matchesLocation &&
            matchesPlatform &&
            matchesType &&
            matchesWorkplace &&
            matchesExperience
        );
    });

    currentJobList = filteredJobs;
    sortJobs();
}

function sortJobs() {
    const sortElement = document.getElementById("sort");
    const sortValue = sortElement ? sortElement.value : "latest";

    if (sortValue === "relevance" && activeFilters.keyword) {
        const keyword = activeFilters.keyword;
        const scoreJob = job => {
            let value = 0;
            if (job.title.toLowerCase().includes(keyword)) value += 3;
            if (job.platform.toLowerCase().includes(keyword)) value += 2;
            if (job.tags.some(tag => tag.toLowerCase().includes(keyword))) value += 1;
            return value;
        };
        currentJobList.sort((a, b) => scoreJob(b) - scoreJob(a));
    } else {
        currentJobList.sort((a, b) => a.id - b.id);
    }

    displayJobs(currentJobList);
    updateJobCount();
}

function updateJobCount() {
    const countElement = document.getElementById("jobResultCount");
    if (countElement) {
        countElement.textContent = `${currentJobList.length} job${currentJobList.length === 1 ? "" : "s"} found`;
    }
}


// =====================================================
// DISPLAY JOBS
// =====================================================

function displayJobs(jobList = currentJobList) {
    const jobContainer = document.getElementById("jobList");
    if (!jobContainer) {
        return;
    }

    if (jobList.length === 0) {
        jobContainer.innerHTML = `
            <div class="empty-state">
                <h3>No jobs found</h3>
                <p>Try changing your search or filters.</p>
                <button type="button" class="btn light" onclick="clearFilters()">
                    Clear filters
                </button>
            </div>
        `;
        updateJobCount();
        return;
    }

    jobContainer.innerHTML = jobList.map(job => `
        <article class="job-card">
            <div class="job-card-header">
                <div>
                    <h3>${escapeHtml(job.title)}</h3>
                    <p class="company">${escapeHtml(job.company)}</p>
                </div>
                <span class="job-platform">${escapeHtml(job.platform)}</span>
            </div>

            <div class="job-details">
                <span>📍 ${escapeHtml(job.location)}</span>
                <span>💼 ${escapeHtml(job.type)}</span>
                <span>💰 ${escapeHtml(job.salary)}</span>
            </div>

            <div class="job-tags">
                ${job.tags.map(tag => `
                    <span class="tag">${escapeHtml(tag)}</span>
                `).join("")}
            </div>

            <div class="job-card-footer">
                <span>${escapeHtml(job.experience)}</span>
                <a href="${pageLink(`job-details.html?id=${job.id}`)}" class="btn">
                    View Job
                </a>
            </div>
        </article>
    `).join("");

    updateJobCount();
}


function searchJobs() {
    const keywordElement = document.getElementById("keyword");
    const locationElement = document.getElementById("location");

    activeFilters.keyword = keywordElement
        ? keywordElement.value.toLowerCase().trim()
        : "";

    activeFilters.location = locationElement
        ? locationElement.value.toLowerCase().trim()
        : "";

    applyFilters();
}

function filterByPlatform(platform) {
    activeFilters.platform = platform || "all";
    applyFilters();
}

function filterByType(type) {
    activeFilters.jobType = type || "all";
    applyFilters();
}

function filterByWorkplace(workplace, checked) {
    if (checked) {
        if (!activeFilters.workplaces.includes(workplace)) {
            activeFilters.workplaces.push(workplace);
        }
    } else {
        activeFilters.workplaces = activeFilters.workplaces.filter(item => item !== workplace);
    }

    applyFilters();
}

function filterByExperience(experience, checked) {
    if (checked) {
        if (!activeFilters.experiences.includes(experience)) {
            activeFilters.experiences.push(experience);
        }
    } else {
        activeFilters.experiences = activeFilters.experiences.filter(item => item !== experience);
    }

    applyFilters();
}

function clearFilters() {
    activeFilters = {
        keyword: "",
        location: "",
        platform: "all",
        jobType: "all",
        workplaces: [],
        experiences: []
    };

    const keywordElement = document.getElementById("keyword");
    const locationElement = document.getElementById("location");
    const sortElement = document.getElementById("sort");

    if (keywordElement) keywordElement.value = "";
    if (locationElement) locationElement.value = "";
    if (sortElement) sortElement.value = "latest";

    const allPlatform = document.querySelector('input[name="platform"][value="all"]');
    const allJobType = document.querySelector('input[name="jobType"][value="all"]');
    if (allPlatform) allPlatform.checked = true;
    if (allJobType) allJobType.checked = true;

    document.querySelectorAll('input[name="workplace"]').forEach(input => {
        input.checked = false;
    });

    document.querySelectorAll('input[name="experience"]').forEach(input => {
        input.checked = false;
    });

    currentJobList = [...jobs];
    displayJobs(currentJobList);
}


function getJobById(id) {
    return jobs.find(job => job.id === Number(id));
}


function displayJobDetails() {
    const container = document.getElementById("jobDetails");
    if (!container) {
        return;
    }

    const params = new URLSearchParams(window.location.search);
    const jobId = params.get("id");
    const job = getJobById(jobId);

    if (!job) {
        container.innerHTML = `
            <div class="empty-state">
                <h2>Job not found</h2>
                <p>The job you are looking for may have been removed or is unavailable.</p>
                <a href="${pageLink("jobs.html")}" class="btn">Back to Jobs</a>
            </div>
        `;
        return;
    }

    document.title = `${job.title} | Ascending Cloud Staffing`;

    container.innerHTML = `
        <article class="job-detail-card">
            <span class="job-platform">${escapeHtml(job.platform)}</span>
            <h1>${escapeHtml(job.title)}</h1>
            <h3>${escapeHtml(job.company)}</h3>

            <div class="job-details">
                <span>📍 ${escapeHtml(job.location)}</span>
                <span>💼 ${escapeHtml(job.type)}</span>
                <span>💰 ${escapeHtml(job.salary)}</span>
                <span>👨‍💻 ${escapeHtml(job.experience)}</span>
            </div>

            <h2>About the role</h2>
            <p class="muted">
                Join a growing technology team and contribute to cloud-first initiatives
                across ${escapeHtml(job.platform)} environments. This ${escapeHtml(job.type.toLowerCase())}
                role is ideal for professionals with ${escapeHtml(job.experience.toLowerCase())} experience.
            </p>

            <h2>Skills Required</h2>
            <div class="job-tags">
                ${job.tags.map(tag => `
                    <span class="tag">${escapeHtml(tag)}</span>
                `).join("")}
            </div>

            <div class="job-actions">
                <a href="${pageLink("login.html")}" class="btn">Apply Now</a>
                <button type="button" class="btn light" onclick="saveJob(${job.id})">
                    Save Job
                </button>
            </div>
        </article>
    `;
}


function saveJob(jobId) {
    let savedJobs = JSON.parse(localStorage.getItem("savedJobs")) || [];

    if (!savedJobs.includes(jobId)) {
        savedJobs.push(jobId);
        localStorage.setItem("savedJobs", JSON.stringify(savedJobs));
        showToast("Job saved successfully!");
    } else {
        showToast("Job is already saved.");
    }
}

function removeSavedJob(jobId) {
    let savedJobs = JSON.parse(localStorage.getItem("savedJobs")) || [];
    savedJobs = savedJobs.filter(id => id !== jobId);
    localStorage.setItem("savedJobs", JSON.stringify(savedJobs));
    showToast("Job removed.");
    loadSavedJobs();
}

function loadSavedJobs() {
    const container = document.getElementById("savedJobsList");
    if (!container) {
        return;
    }

    const savedIds = JSON.parse(localStorage.getItem("savedJobs")) || [];
    const savedJobItems = jobs.filter(job => savedIds.includes(job.id));

    if (savedJobItems.length === 0) {
        container.innerHTML = `
            <div class="empty-state compact">
                <p class="muted">You have not saved any jobs yet.</p>
                <a href="${pageLink("jobs.html")}" class="btn light">Browse Jobs</a>
            </div>
        `;
        updateSavedJobCount(0);
        return;
    }

    updateSavedJobCount(savedJobItems.length);

    container.innerHTML = savedJobItems.map(job => `
        <div class="panel saved-job-item">
            <div>
                <h3>${escapeHtml(job.title)}</h3>
                <p class="muted">${escapeHtml(job.company)} · ${escapeHtml(job.location)}</p>
            </div>
            <div class="saved-job-actions">
                <a href="${pageLink(`job-details.html?id=${job.id}`)}" class="btn light">View</a>
                <button type="button" class="btn outline" onclick="removeSavedJob(${job.id})">
                    Remove
                </button>
            </div>
        </div>
    `).join("");
}

function updateSavedJobCount(count) {
    const countElement = document.getElementById("savedJobCount");
    if (countElement) {
        countElement.textContent = String(count);
    }
}


function showToast(message) {
    let toast = document.getElementById("toast");

    if (!toast) {
        toast = document.createElement("div");
        toast.id = "toast";
        toast.setAttribute("role", "status");
        toast.setAttribute("aria-live", "polite");
        document.body.appendChild(toast);
    }

    toast.textContent = message;
    toast.classList.add("show");

    clearTimeout(showToast.timeoutId);
    showToast.timeoutId = setTimeout(() => {
        toast.classList.remove("show");
    }, 3000);
}


function toggleMenu(button) {
    const navLinks = document.getElementById("primaryNav");
    if (!navLinks) {
        return;
    }

    const isOpen = navLinks.classList.toggle("active");
    if (button) {
        button.setAttribute("aria-expanded", isOpen ? "true" : "false");
    }
}


function isLoggedIn() {
    return localStorage.getItem("loggedIn") === "true";
}

function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function loginUser() {
    const emailElement = document.getElementById("email");
    const passwordElement = document.getElementById("password");
    const roleElement = document.getElementById("role");

    const email = emailElement ? emailElement.value.trim() : "";
    const password = passwordElement ? passwordElement.value : "";

    if (!email || !password || !roleElement) {
        showToast("Enter your email and password.");
        return;
    }

    if (!isValidEmail(email)) {
        showToast("Enter a valid email address.");
        return;
    }

    if (password.length < 6) {
        showToast("Password must be at least 6 characters.");
        return;
    }

    localStorage.setItem("loggedIn", "true");
    localStorage.setItem("userRole", roleElement.value);
    localStorage.setItem("userEmail", email);

    showToast("Login successful.");

    setTimeout(() => {
        window.location.href = pageLink(getDashboardPath(roleElement.value));
    }, 500);
}

function signupUser() {
    const nameElement = document.getElementById("name");
    const emailElement = document.getElementById("email");
    const passwordElement = document.getElementById("password");
    const roleElement = document.getElementById("role");

    const name = nameElement ? nameElement.value.trim() : "";
    const email = emailElement ? emailElement.value.trim() : "";
    const password = passwordElement ? passwordElement.value : "";

    if (!name || !email || !password || !roleElement) {
        showToast("Please complete all required fields.");
        return;
    }

    if (!isValidEmail(email)) {
        showToast("Enter a valid email address.");
        return;
    }

    if (password.length < 6) {
        showToast("Password must be at least 6 characters.");
        return;
    }

    localStorage.setItem("loggedIn", "true");
    localStorage.setItem("userRole", roleElement.value);
    localStorage.setItem("userEmail", email);
    localStorage.setItem("userName", name);

    showToast("Account created successfully.");

    setTimeout(() => {
        window.location.href = pageLink(getDashboardPath(roleElement.value));
    }, 500);
}

function logout() {
    localStorage.removeItem("loggedIn");
    localStorage.removeItem("userRole");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("userName");

    showToast("Logged out successfully.");

    setTimeout(() => {
        window.location.href = pageLink("index.html");
    }, 800);
}

function protectPage(requiredRole = null) {
    if (!isLoggedIn()) {
        window.location.href = pageLink("login.html");
        return false;
    }

    if (requiredRole) {
        const userRole = localStorage.getItem("userRole");

        if (userRole !== requiredRole) {
            showToast("You do not have permission to access this page.");
            setTimeout(() => {
                window.location.href = pageLink("index.html");
            }, 1000);
            return false;
        }
    }

    return true;
}

function loadStats() {
    const jobCount = document.getElementById("jobCount");
    if (jobCount) {
        jobCount.textContent = `${jobs.length}+`;
    }

    const candidateCount = document.getElementById("candidateCount");
    if (candidateCount) {
        candidateCount.textContent = "500+";
    }

    const employerCount = document.getElementById("employerCount");
    if (employerCount) {
        employerCount.textContent = "100+";
    }
}

function loadFeaturedJobs() {
    const container = document.getElementById("featuredJobs");
    if (!container) {
        return;
    }

    const featured = jobs.slice(0, 3);

    container.innerHTML = featured.map(job => `
        <article class="job-card">
            <span class="job-platform">${escapeHtml(job.platform)}</span>
            <h3>${escapeHtml(job.title)}</h3>
            <p>${escapeHtml(job.company)}</p>
            <div class="job-details">
                <span>📍 ${escapeHtml(job.location)}</span>
                <span>💼 ${escapeHtml(job.type)}</span>
            </div>
            <p><strong>${escapeHtml(job.salary)}</strong></p>
            <a href="${pageLink(`job-details.html?id=${job.id}`)}" class="btn">
                View Job
            </a>
        </article>
    `).join("");
}

function submitContactForm(event) {
    if (event) {
        event.preventDefault();
    }

    const nameElement = document.getElementById("contactName");
    const emailElement = document.getElementById("contactEmail");
    const messageElement = document.getElementById("contactMessage");

    const name = nameElement ? nameElement.value.trim() : "";
    const email = emailElement ? emailElement.value.trim() : "";
    const message = messageElement ? messageElement.value.trim() : "";

    if (!name || !email || !message) {
        showToast("Please complete all required fields.");
        return;
    }

    if (!isValidEmail(email)) {
        showToast("Enter a valid email address.");
        return;
    }

    showToast("Thank you! We will contact you soon.");

    const form = document.getElementById("contactForm");
    if (form) {
        form.reset();
    }
}

function submitHireForm(event) {
    if (event) {
        event.preventDefault();
    }

    const companyElement = document.getElementById("companyName");
    const emailElement = document.getElementById("hireEmail");
    const roleElement = document.getElementById("hireRole");

    const company = companyElement ? companyElement.value.trim() : "";
    const email = emailElement ? emailElement.value.trim() : "";
    const role = roleElement ? roleElement.value.trim() : "";

    if (!company || !email || !role) {
        showToast("Please complete all required fields.");
        return;
    }

    if (!isValidEmail(email)) {
        showToast("Enter a valid email address.");
        return;
    }

    showToast("Request submitted. Our team will reach out shortly.");

    const form = document.getElementById("hireForm");
    if (form) {
        form.reset();
    }
}

function submitJobForm(event) {
    if (event) {
        event.preventDefault();
    }

    if (!protectPage("employer")) {
        return;
    }

    showToast("Job posting saved as a draft.");
    const form = document.getElementById("postJobForm");
    if (form) {
        form.reset();
    }
}

function subscribeNewsletter(event) {
    if (event) {
        event.preventDefault();
    }

    const emailInput = document.getElementById("newsletterEmail");

    if (!emailInput || !emailInput.value.trim()) {
        showToast("Please enter your email.");
        return;
    }

    if (!isValidEmail(emailInput.value.trim())) {
        showToast("Enter a valid email address.");
        return;
    }

    showToast("You have subscribed successfully!");
    emailInput.value = "";
}

function initLoginPage() {
    const params = new URLSearchParams(window.location.search);
    const role = params.get("role");
    const roleElement = document.getElementById("role");

    if (role && roleElement) {
        roleElement.value = role;
    }
}

function initJobsPageFromQuery() {
    const params = new URLSearchParams(window.location.search);
    const keyword = params.get("keyword");
    const location = params.get("location");

    const keywordElement = document.getElementById("keyword");
    const locationElement = document.getElementById("location");

    if (keyword && keywordElement) {
        keywordElement.value = keyword;
        activeFilters.keyword = keyword.toLowerCase();
    }

    if (location && locationElement) {
        locationElement.value = location;
        activeFilters.location = location.toLowerCase();
    }

    if (keyword || location) {
        applyFilters();
    }
}

function initHeroSearch() {
    const heroSearchButton = document.getElementById("heroSearchBtn");
    const heroSearchInput = document.getElementById("heroSearch");
    const heroLocationInput = document.getElementById("heroLocation");

    if (!heroSearchButton) {
        return;
    }

    heroSearchButton.addEventListener("click", function (event) {
        event.preventDefault();

        const keyword = heroSearchInput ? heroSearchInput.value.trim() : "";
        const location = heroLocationInput ? heroLocationInput.value.trim() : "";
        const params = new URLSearchParams();

        if (keyword) {
            params.set("keyword", keyword);
        }

        if (location) {
            params.set("location", location);
        }

        const query = params.toString();
        window.location.href = pageLink(`../jobs.html${query ? `?${query}` : ""}`);
    });
}

function initSortControl() {
    const sortElement = document.getElementById("sort");
    if (sortElement) {
        sortElement.addEventListener("change", sortJobs);
    }
}

function loadDashboardUser() {
    const nameElement = document.getElementById("dashboardUserName");
    const emailElement = document.getElementById("dashboardUserEmail");

    const name = localStorage.getItem("userName");
    const email = localStorage.getItem("userEmail");

    if (nameElement) {
        nameElement.textContent = name || "User";
    }

    if (emailElement) {
        emailElement.textContent = email || "";
    }
}


// =====================================================
// INITIALIZE WEBSITE
// =====================================================

document.addEventListener("DOMContentLoaded", function () {
    loadNavbar();
    loadFooter();
    loadStats();
    loadFeaturedJobs();
    initLoginPage();
    initHeroSearch();
    initSortControl();
    initJobsPageFromQuery();
    displayJobs();
    displayJobDetails();
    loadSavedJobs();
    loadDashboardUser();
});
