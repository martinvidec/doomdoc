# GitHub Configuration

This directory contains GitHub-specific configuration for the DoomDoc project.

## Contents

### Workflows

**jira-integration.yml**
- Validates PR titles contain JIRA ticket reference `[DOOM-XXX]`
- Validates branch names follow pattern `<type>/DOOM-XXX-<description>`
- Builds project and generates documentation
- Optionally updates JIRA ticket status (requires API setup)

Runs on: Pull requests and pushes to main

### Documentation

**JIRA_API_SETUP.md**
- Complete guide for setting up JIRA API integration
- Instructions for creating API tokens
- How to configure GitHub secrets
- Troubleshooting guide

## Quick Start

### 1. Install Git Hooks Locally
```bash
./scripts/setup-hooks.sh
```

This installs local validation that runs before commits and pushes.

### 2. Enable JIRA API Integration (Optional)
Follow the instructions in `JIRA_API_SETUP.md` to enable automatic JIRA updates.

### 3. Create a Feature Branch
```bash
git checkout -b feature/DOOM-123-your-feature-description
```

### 4. Commit with JIRA Reference
```bash
git commit -m "DOOM-123: Add your feature description"
```

### 5. Push and Create PR
```bash
git push -u origin feature/DOOM-123-your-feature-description
```

Create PR with title: `[DOOM-123] Your feature description`

## Workflow Overview

```
JIRA Ticket → Feature Branch → Commits → Pull Request → Review → Merge → Done
   ↓              ↓               ↓           ↓            ↓        ↓
 DOOM-123    DOOM-123 in    DOOM-123:    [DOOM-123]   GitHub   Merged to
             branch name   in commit     in PR title   Review     main
```

## Additional Resources

- **Main Workflow Documentation**: See `/WORKFLOW.md` in repository root
- **Project Documentation**: See `/CLAUDE.md` and `/DATA_STRUCTURES.md`
- **GitHub Actions**: https://github.com/features/actions
- **JIRA Integration**: https://support.atlassian.com/jira-cloud-administration/docs/integrate-with-development-tools/
