#!/bin/bash

# Setup script for installing Git hooks
# This script copies the custom Git hooks to .git/hooks/

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== DoomDoc Git Hooks Setup ===${NC}"
echo ""

# Check if we're in a git repository
if [ ! -d ".git" ]; then
    echo -e "${RED}✗ Error: Not in a git repository${NC}"
    echo "Please run this script from the repository root"
    exit 1
fi

# Check if hooks directory exists
if [ ! -d "scripts/git-hooks" ]; then
    echo -e "${RED}✗ Error: scripts/git-hooks directory not found${NC}"
    exit 1
fi

echo "Installing Git hooks..."
echo ""

# Install commit-msg hook
if [ -f "scripts/git-hooks/commit-msg" ]; then
    cp scripts/git-hooks/commit-msg .git/hooks/commit-msg
    chmod +x .git/hooks/commit-msg
    echo -e "${GREEN}✓${NC} Installed commit-msg hook (validates JIRA ticket in commits)"
else
    echo -e "${YELLOW}⚠${NC} commit-msg hook not found, skipping"
fi

# Install pre-push hook
if [ -f "scripts/git-hooks/pre-push" ]; then
    cp scripts/git-hooks/pre-push .git/hooks/pre-push
    chmod +x .git/hooks/pre-push
    echo -e "${GREEN}✓${NC} Installed pre-push hook (validates branch naming)"
else
    echo -e "${YELLOW}⚠${NC} pre-push hook not found, skipping"
fi

echo ""
echo -e "${GREEN}=== Setup Complete ===${NC}"
echo ""
echo "The following hooks are now active:"
echo "  • commit-msg: Ensures commit messages start with DOOM-XXX"
echo "  • pre-push:   Ensures branch names follow <type>/DOOM-XXX-<description>"
echo ""
echo "To bypass hooks (not recommended):"
echo "  git commit --no-verify"
echo "  git push --no-verify"
echo ""
echo "For more information, see WORKFLOW.md"
