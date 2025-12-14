# JIRA API Integration Setup

This document explains how to enable automatic JIRA status updates from GitHub Actions.

## Overview

The GitHub Actions workflow can automatically:
- Add PR links to JIRA tickets as comments
- Transition tickets to "In Review" when PRs are opened
- Transition tickets to "Done" when PRs are merged

## Prerequisites

1. JIRA Cloud or Server instance
2. JIRA user account with API access
3. GitHub repository admin access (to add secrets)

## Setup Steps

### 1. Create JIRA API Token

**For JIRA Cloud:**
1. Log in to https://id.atlassian.com/manage-profile/security/api-tokens
2. Click "Create API token"
3. Give it a name (e.g., "GitHub Actions - DoomDoc")
4. Copy the token (you won't see it again)

**For JIRA Server/Data Center:**
1. Use your JIRA password or create a Personal Access Token
2. See: https://confluence.atlassian.com/enterprise/using-personal-access-tokens-1026032365.html

### 2. Add GitHub Secrets

Go to your GitHub repository: Settings → Secrets and variables → Actions → New repository secret

Add these three secrets:

**JIRA_BASE_URL**
- Value: Your JIRA instance URL
- Example: `https://your-company.atlassian.net`
- No trailing slash

**JIRA_USER_EMAIL**
- Value: Email address of the JIRA user
- Example: `your-email@company.com`

**JIRA_API_TOKEN**
- Value: The API token from Step 1
- This is sensitive - keep it secret

### 3. Find JIRA Transition IDs

JIRA transitions (status changes) require specific IDs. To find them:

```bash
# Replace with your values
JIRA_BASE_URL="https://your-company.atlassian.net"
JIRA_USER_EMAIL="your-email@company.com"
JIRA_API_TOKEN="your-api-token"
ISSUE_KEY="DOOM-1"

# Get available transitions for an issue
curl -u "$JIRA_USER_EMAIL:$JIRA_API_TOKEN" \
  "$JIRA_BASE_URL/rest/api/3/issue/$ISSUE_KEY/transitions" | jq '.transitions[] | {id, name}'
```

This returns something like:
```json
{"id": "11", "name": "To Do"}
{"id": "21", "name": "In Progress"}
{"id": "31", "name": "In Review"}
{"id": "41", "name": "Done"}
```

Note the ID for "In Review" (for PR opened) and "Done" (for PR merged).

### 4. Update GitHub Actions Workflow

Edit `.github/workflows/jira-integration.yml`:

1. **Enable the `update-jira` job:**
   Remove or comment out the `if: false` condition

2. **Update transition IDs:**
   Replace `<transition-id-to-in-review>` with the actual ID from Step 3

3. **Uncomment API calls:**
   Uncomment the `curl` commands in the workflow

Example updated section:
```yaml
- name: Transition JIRA to In Review
  if: steps.extract.outputs.jira_key != '' && github.event.action == 'opened'
  run: |
    curl -X POST "${{ secrets.JIRA_BASE_URL }}/rest/api/3/issue/${{ steps.extract.outputs.jira_key }}/transitions" \
      -H "Content-Type: application/json" \
      -u "${{ secrets.JIRA_USER_EMAIL }}:${{ secrets.JIRA_API_TOKEN }}" \
      -d '{"transition":{"id":"31"}}'
```

### 5. Test the Integration

1. Create a test JIRA ticket (e.g., DOOM-999)
2. Create a branch: `git checkout -b feature/DOOM-999-test-integration`
3. Make a small change and commit
4. Push and create a PR with title: `[DOOM-999] Test JIRA integration`
5. Check:
   - GitHub Actions runs successfully
   - JIRA ticket has PR link in comments
   - JIRA ticket status changed to "In Review"

## Workflow Triggers

The JIRA integration is triggered on:

- **PR opened**: Adds PR link to JIRA, transitions to "In Review"
- **PR merged**: Transitions to "Done"
- **PR closed (without merge)**: No automatic transition (manual JIRA update needed)

## Troubleshooting

### Authentication Errors (401)
- Verify JIRA_USER_EMAIL is correct
- Regenerate JIRA_API_TOKEN
- Check JIRA_BASE_URL has no trailing slash

### Transition Errors (400)
- Verify transition ID is correct for your workflow
- Check JIRA workflow allows the transition from current status
- Some transitions require fields to be filled

### Permission Errors (403)
- Ensure JIRA user has permission to comment and transition issues
- Check project permissions in JIRA

### Logs
View detailed logs:
1. Go to GitHub Actions tab in your repository
2. Click on the failed workflow run
3. Expand the "Update JIRA Status" job
4. Check the logs for specific error messages

## Security Notes

- Never commit API tokens to the repository
- Use GitHub Secrets for all sensitive credentials
- Rotate API tokens periodically
- Use a dedicated JIRA service account (not your personal account)
- Limit JIRA account permissions to minimum required

## Optional Enhancements

### Add PR Merged Transition
Add a new job for merged PRs:

```yaml
update-jira-merged:
  name: Update JIRA on Merge
  runs-on: ubuntu-latest
  if: github.event.pull_request.merged == true

  steps:
    - name: Extract JIRA Issue Key
      # ... (same as before)

    - name: Transition JIRA to Done
      run: |
        curl -X POST "${{ secrets.JIRA_BASE_URL }}/rest/api/3/issue/${{ steps.extract.outputs.jira_key }}/transitions" \
          -H "Content-Type: application/json" \
          -u "${{ secrets.JIRA_USER_EMAIL }}:${{ secrets.JIRA_API_TOKEN }}" \
          -d '{"transition":{"id":"41"}}'
```

### Add Build Status to JIRA
Update JIRA with build/test results:

```yaml
- name: Add Build Status Comment
  if: always()
  run: |
    STATUS="${{ job.status }}"
    curl -X POST "${{ secrets.JIRA_BASE_URL }}/rest/api/3/issue/${{ steps.extract.outputs.jira_key }}/comment" \
      -H "Content-Type: application/json" \
      -u "${{ secrets.JIRA_USER_EMAIL }}:${{ secrets.JIRA_API_TOKEN }}" \
      -d "{\"body\": {\"type\": \"doc\", \"version\": 1, \"content\": [{\"type\": \"paragraph\", \"content\": [{\"type\": \"text\", \"text\": \"Build status: $STATUS\"}]}]}}"
```

## References

- [JIRA Cloud REST API Documentation](https://developer.atlassian.com/cloud/jira/platform/rest/v3/intro/)
- [GitHub Actions Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [JIRA API Authentication](https://developer.atlassian.com/cloud/jira/platform/basic-auth-for-rest-apis/)
