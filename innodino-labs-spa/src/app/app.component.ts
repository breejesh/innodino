import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'InnoDino Labs';

  ngOnInit() {
    // Initialize gallery tabs functionality
    this.initializeGalleryTabs();
  }

  // Video switching functionality
  switchVideo(type: string) {
    const iframe = document.getElementById('demo-video') as HTMLIFrameElement;
    const trailerBtn = document.getElementById('trailer-btn');
    const presentationBtn = document.getElementById('presentation-btn');
    const description = document.getElementById('video-description');

    // YouTube parameters to remove branding and enable autoplay
    const youtubeParams = '?modestbranding=1&rel=0&showinfo=0&controls=1&cc_load_policy=0&iv_load_policy=3&loop=1';

    if (type === 'trailer') {
      iframe.src = `https://www.youtube.com/embed/7SP1aFBtbIY${youtubeParams}`;
      trailerBtn?.classList.add('active');
      presentationBtn?.classList.remove('active');
      if (description) {
        description.textContent = 'Quick overview of InnoDino\'s key features and student experiences';
      }
    } else {
      iframe.src = `https://www.youtube.com/embed/Pboqs_4Z1Ag${youtubeParams}`;
      presentationBtn?.classList.add('active');
      trailerBtn?.classList.remove('active');
      if (description) {
        description.textContent = 'Comprehensive presentation covering curriculum, pedagogy, and implementation';
      }
    }
  }

  // Gallery tabs functionality
  initializeGalleryTabs() {
    // Wait for DOM to be ready
    setTimeout(() => {
      const galleryTabs = document.querySelectorAll('.gallery-tab');
      const galleryPanels = document.querySelectorAll('.gallery-panel');

      galleryTabs.forEach(tab => {
        tab.addEventListener('click', () => {
          const module = tab.getAttribute('data-module');

          // Remove active class from all tabs and panels
          galleryTabs.forEach(t => t.classList.remove('active'));
          galleryPanels.forEach(p => p.classList.remove('active'));

          // Add active class to clicked tab
          tab.classList.add('active');

          // Show corresponding panel
          const activePanel = document.getElementById(`${module}-gallery`);
          if (activePanel) {
            activePanel.classList.add('active');
          }
        });
      });
    }, 100);
  }
}
