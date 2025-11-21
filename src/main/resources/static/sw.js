const CACHE_NAME = 'expense-tracker-v1';
const ASSETS = [
    './',
    './index.html',
    './dashboard.html',
    './manage.html',
    './history.html',
    './profile.html',
    './styles.css',
    './app.js',
    './icon.svg',
    './manifest.json'
];

self.addEventListener('install', (event) => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then((cache) => cache.addAll(ASSETS))
    );
});

self.addEventListener('fetch', (event) => {
    // For API requests, go to network first, don't cache
    if (event.request.url.includes('/api/')) {
        return;
    }

    event.respondWith(
        caches.match(event.request)
            .then((response) => response || fetch(event.request))
    );
});
